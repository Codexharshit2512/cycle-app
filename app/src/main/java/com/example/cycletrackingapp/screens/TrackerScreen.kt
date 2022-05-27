package com.example.cycletrackingapp.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.cycletrackingapp.R
import com.example.cycletrackingapp.databinding.FragmentTrackerScreenBinding
import com.example.cycletrackingapp.services.LocationService
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.cycletrackingapp.CustomApplication
import com.example.cycletrackingapp.custom_views.PresentationalDialog
import com.example.cycletrackingapp.services.PathPoints
import com.example.cycletrackingapp.utils.Constant
import com.example.cycletrackingapp.utils.PermissionUtility
import com.example.cycletrackingapp.viewModels.HistoryViewModel
import com.example.cycletrackingapp.viewModels.ModelFactory
import com.example.cycletrackingapp.viewModels.TrackerViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TrackerScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class TrackerScreen : Fragment(),OnMapReadyCallback,GoogleMap.SnapshotReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentTrackerScreenBinding
    private lateinit var permissionLauncher:ActivityResultLauncher<Array<String>>
    private lateinit var dialog:AlertDialog
    private lateinit var map:GoogleMap
    private lateinit var viewModel:TrackerViewModel
    private var loadingDialog:PresentationalDialog?=null
    private val TAG="TRACKER SCREEN"
    private var userLocationMarker: Marker? = null
    private var isLocked=false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentTrackerScreenBinding.inflate(inflater,container,false)
        val mapFragment=childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        askForLocationPermissions()
        initViewModel()
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map=googleMap
        addActionButtonListeners()
        if(PermissionUtility.hasLocationPermissions(requireContext())) enableCurrentLocation()
    }

    private fun initViewModel(){
        val cApp = requireActivity().application as CustomApplication
        val factory = ModelFactory(cApp.repo,Constant.TRACKER_VIEWMODEL_CODE)
        viewModel = ViewModelProvider(this,factory).get(TrackerViewModel::class.java)
        addRunInfoObservers()
    }

    /** Tracking actions **/
    private fun addActionButtonListeners(){
        binding.apply {
            startBtn.setOnClickListener {
                if(LocationService.tracking.value==Constant.TRACKING_NOT_STARTED){
                    setLocationChangeObservers()
                    startTrackingUser()
                }
            }

            trackingContainer.apply {
                pauseBtn.setOnClickListener {
                    if(!isLocked && LocationService.tracking.value==Constant.TRACKING_IN_PROGRESS){
                        pauseTrackingUser()
                    }
                    else if(!isLocked && LocationService.tracking.value==Constant.TRACKING_PAUSED){
                        startTrackingUser()
                    }
                }

                stopBtn.setOnClickListener {
                    if(!isLocked
                        && (LocationService.tracking.value==Constant.TRACKING_IN_PROGRESS
                                || LocationService.tracking.value==Constant.TRACKING_PAUSED)
                    ){
                        stopTrackingUser()

                    }
                }

                lockContainer.setOnClickListener {
                    if(isLocked){
                       unlockTrackingButtons()
                    }
                    else lockTrackingButtons()
                }
            }
        }
    }

    private fun markStartTime(){
        viewModel.startTime=System.currentTimeMillis()
    }

    private fun startTrackingUser(){
        Intent(requireContext(),LocationService::class.java).also { intent->
            intent.action= Constant.START_OR_RESUME_LOCATION_SERVICE
            requireContext().startService(intent)
        }
        toggleActionButtons(true)
        binding.trackingContainer.pauseBtn.text = resources.getString(R.string.pause_text)
//        binding.actionBtn.text=requireContext().getString(R.string.stop_tracking)
//        stopWatch?.startTimer()
    }

    private fun pauseTrackingUser(){
//        stopWatch?.pauseTimer()
            Intent(requireContext(),LocationService::class.java).also { intent->
                intent.action=Constant.PAUSE_LOCATION_SERVICE
                requireContext().startService(intent)
            }
            binding.trackingContainer.pauseBtn.text = resources.getString(R.string.resume_text)

    }

    private fun stopTrackingUser(){
           Intent(requireContext(),LocationService::class.java).also { intent->
                intent.action=Constant.STOP_LOCATION_SERVICE
                requireContext().startService(intent)
           }
        zoomOutMapToIncludeRegion()
//           findNavController().navigate(R.id.action_trackerScreen_to_historyFragment)
    }


    private fun toggleActionButtons(flag:Boolean){
        if(flag) showTrackingUI()
        else hideTrackingUI()
    }

    private fun showTrackingUI(){
        binding.trackingContainer.root.visibility=View.VISIBLE
        lockTrackingButtons()
        toggleStartButton(false)
    }


    private fun hideTrackingUI(){
        binding.trackingContainer.root.visibility=View.GONE
        unlockTrackingButtons()
        toggleStartButton(true)
    }

    private fun toggleStartButton(flag:Boolean){
        if(flag) binding.startBtn.visibility=View.VISIBLE
        else binding.startBtn.visibility=View.GONE
    }

    private fun disableTrackingButtons(){
        val id=R.color.disabled_btn
        binding.trackingContainer.pauseBtn.setBackgroundColor(resources.getColor(id))
        binding.trackingContainer.stopBtn.setBackgroundColor(resources.getColor(id))
    }

    private fun enableTrackingButtons(){
        val pauseColorId=R.color.royal_blue
        val stopColorId=R.color.stop_color
        binding.trackingContainer.pauseBtn.setBackgroundColor(resources.getColor(pauseColorId))
        binding.trackingContainer.stopBtn.setBackgroundColor(resources.getColor(stopColorId))
    }

    private fun lockTrackingButtons(){
        isLocked=true
        binding.trackingContainer.lock.setImageResource(R.drawable.outline_lock_24)
       disableTrackingButtons()
    }

    private fun unlockTrackingButtons(){
        isLocked=false
        binding.trackingContainer.lock.setImageResource(R.drawable.outline_lock_open_24)
        enableTrackingButtons()
    }

    private fun addRunInfoObservers(){
     viewModel.runInfo.observe(viewLifecycleOwner, Observer {
          Log.i(TAG,"run updated $it")
           binding.apply {
               trackerDistance.infoText=it.distance.toString()
               trackerCalories.infoText=it.caloriesBurned.toString()
               trackerSpeed.infoText=it.speed.toString()
           }
       })
    }


    /** Tracking actions **/






    //** Map Actions **/
    private fun setLocationChangeObservers(){
        LocationService.pathPoints.observe(viewLifecycleOwner, Observer {
            addPolylineToMap(it)
            if(it.isNotEmpty() && it.last().isNotEmpty()) updateMarkerOnTheMap(it?.last()?.last())
        })
    }

    private fun updateMarkerOnTheMap(loc: Location?) {
        loc ?: return
        val location= LatLng(loc.latitude,loc.longitude)
        if(userLocationMarker==null){
            val markerOptions = MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                .position(location)
                .anchor(0.5f,0.5f)
                .rotation(loc.bearing)
            userLocationMarker=map.addMarker(markerOptions)
        }
        else{
            userLocationMarker?.position=location
            userLocationMarker?.rotation = loc.bearing
        }
    }

    private fun addPolylineToMap(pathPoints:PathPoints){
        if(pathPoints.size>0 && pathPoints.last().size>1){
            val size=pathPoints.last().size
            val p1=pathPoints.last()[size-2]
            val p2=pathPoints.last()[size-1]
            val polylineOptions= PolylineOptions()
                .clickable(false)
                .color(Color.RED)
                .width(12f)
                .add(LatLng(p1.latitude,p1.longitude), LatLng(p2.latitude,p2.longitude))
            map.addPolyline(polylineOptions)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(p2.latitude,p2.longitude),18f))
        }
    }
    private fun addPolylinesToMap(){
        val pathPoints = LocationService.pathPoints.value
        pathPoints ?: return
        for(list in pathPoints){
            for(i in list.indices){
                if(i-1>0){
                    val loc2=list[i]
                    val loc1=list[i-1]
                    val polylineOptions= PolylineOptions()
                        .clickable(false)
                        .color(Color.RED)
                        .width(12f)
                        .add(LatLng(loc1.latitude,loc1.longitude), LatLng(loc2.latitude,loc2.longitude))
                    map.addPolyline(polylineOptions)
                }
            }
        }
        val size=pathPoints.size
        if(pathPoints.isNotEmpty() && size>1 && pathPoints[size-2].isNotEmpty()){
            Log.i(TAG,"adding zoom on start")
            val lastLocation = LatLng(pathPoints[size-2].last().latitude,pathPoints[size-2].last().longitude)
            updateMarkerOnTheMap(pathPoints[size-2].last())
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lastLocation.latitude,lastLocation.longitude),18f))
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableCurrentLocation(){
        if(this::map.isInitialized){
//            map.isMyLocationEnabled=true

        }
    }


    /** Map actions **/


    private fun askForLocationPermissions(){
        if(PermissionUtility.hasLocationPermissions(requireContext())){
            enableCurrentLocation()
            return
        }
        initializePermissionLauncher()
        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
        ))
    }


    private fun initializePermissionLauncher(){
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            Log.i(TAG,"permission-> $permissions")
            var flag=true
            permissions.entries.forEach{
                if(!flag) return@forEach
                val pName=it.key
                val granted=it.value
                if(pName==Manifest.permission.ACCESS_COARSE_LOCATION){
                    if(!granted){
                        val bool=shouldShowRequestPermissionRationale(pName)
                        Log.i(TAG,"PERMISSION  $pName -> $bool")
                        if(bool){
                            // show dialog
                            createRationaleDialog(Constant.LOCATION_PERMISSION_RATIONALE)
                            showDialog()
                        }
                        flag=false
                    }
                }
                else{
                    if(!granted && PermissionUtility.hasCoarseLocationPermission(requireContext())){
                        createRationaleDialog(Constant.PRECISE_LOCATION_RATIONALE)
                        showDialog()
//                        val bool=shouldShowRequestPermissionRationale(pName)
//                        Log.i(TAG,"PERMISSION  $pName -> $bool")
//                        if(bool){
//                            // show dialog
//                            createRationaleDialog(Constant.PRECISE_LOCATION_RATIONALE)
//                            showDialog()
//                        }
                    }
                }
            }
        }
    }

    private fun createRationaleDialog(type:String){
        var message=""
        if(type==Constant.PRECISE_LOCATION_RATIONALE) message=resources.getString(R.string.precise_permission_rationale)
        else message=resources.getString(R.string.location_permission_rationale)
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setPositiveButton(R.string.positive_response,
                DialogInterface.OnClickListener { _, _ ->
                    permissionLauncher.launch(arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
                    ))
                })
        dialog=builder.create()
    }

    private fun hideDialog(){
        dialog.hide()
    }

    private fun showDialog(){
        dialog.show()
    }


    //** MAP SNAPSHOT CODE *//
    private fun zoomOutMapToIncludeRegion(){
        LocationService.pathPoints.value?.let{
            val bounds = LatLngBounds.builder()
            for(list in it){
               for(loc in list){
                   val temp = LatLng(loc.latitude,loc.longitude)
                   bounds.include(temp)
               }
            }
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),30))
            takeMapSnapshot()
        }
    }


    private fun takeMapSnapshot(){
        map.snapshot(this)
    }

    override fun onSnapshotReady(snapshot: Bitmap?) {
        viewModel.setSnapshot(snapshot)
        viewModel.endRun(findNavController())
        uploadLoaderObserver()
    }

    private fun uploadLoaderObserver(){
        viewModel.uploadingLoading.observe(viewLifecycleOwner, Observer {
            if(it){
                loadingDialog = PresentationalDialog(R.layout.loading_view)
                loadingDialog?.show(childFragmentManager,"")
            }
            else{
                loadingDialog?.dismiss()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TrackerScreen.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TrackerScreen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}