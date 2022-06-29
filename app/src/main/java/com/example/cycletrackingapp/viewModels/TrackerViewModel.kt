package com.example.cycletrackingapp.viewModels

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.cycletrackingapp.R

import com.example.cycletrackingapp.Repository.MainRepository
import com.example.cycletrackingapp.models.Coordinate
import com.example.cycletrackingapp.models.MiniRunInfo
import com.example.cycletrackingapp.models.Run
import com.example.cycletrackingapp.services.LocationService
import com.example.cycletrackingapp.services.PathPoints
import com.example.cycletrackingapp.utils.MathUtils
import com.example.cycletrackingapp.utils.MathUtils.roundDecimal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.pow

class TrackerViewModel(private val repo:MainRepository) : ViewModel() {
    val runInfo:LiveData<MiniRunInfo> = Transformations.map(LocationService.pathPoints){
        val distance = calculateDistance(it)
        val speed = getSpeed(it)
        val calories = getCalories(it)
        MiniRunInfo(distance, speed,calories)
    }
    private var snapshot:Bitmap?=null
    var startTime:Long=0
    val uploadingLoading = MutableLiveData<Boolean>(false)
    private fun calculateDistance(list:PathPoints):Double{
        if(runInfo.value==null) return 0.0
        if(list.size > 0 && list.last().size>1){
            val size = list.last().size
            val latestLocation = list.last().last()
            val previousLocation = list.last()[size-2]
            val dist = previousLocation.distanceTo(latestLocation) + runInfo.value?.distance!!*1000
            return roundDecimal(2,dist/1000)
        }
        return 0.0
    }

    private fun getSpeed(list:PathPoints):Double{
        if(runInfo.value==null) return 0.0
        if(list.size > 0 && list.last().size>0){
            val speed=list.last().last().speed.toDouble()
            return roundDecimal(2,speed)
        }
        return 0.0
    }

    private fun getCalories(list:PathPoints):Double{
        if(runInfo.value==null) return 0.0
        if(list.size>0 && list.last().size>0){
            val loc=list.last().last()
            val MET=8.0
            val weight=80.0
            val timeDiff=(loc.time-startTime)/1000/60
            return MET*weight*timeDiff
        }
        return 0.0
    }

    fun endRun(controller: NavController){
        snapshot?.let {
            uploadImage(it,controller)
        }
    }

//    private fun createCoordinatesList():List<List<Coordinate>>{
//        val coodList:MutableList<List<Coordinate>> = mutableListOf()
//        val locList = LocationService.pathPoints.value!!
//        for(j in locList.indices){
//            val list = locList[j]
//            val ans = mutableListOf<Coordinate>()
//            for(i in list.indices){
//                val location = list[i]
//                var newDistance = 0.0
//                var distanceFromLastPoint=0.0
//                if(i-1>=0){
//                    val previousCoordinate=ans[i-1]
//                    val previousLocation = list[i-1]
//                    distanceFromLastPoint = previousLocation.distanceTo(location).toDouble()
//                    newDistance= previousCoordinate.distance + distanceFromLastPoint
//                }
//                else if(j-1>=0){
//                    val previousCoordinate=coodList[j-1].last()
//                    newDistance= previousCoordinate.distance
//                }
//                val coordinate = Coordinate(
//                    location.latitude,
//                    location.longitude,
//                    location.speed.toDouble(),
//                    newDistance,
//                    distanceFromLastPoint,
//                    location.altitude,
//                    location.time
//                )
//                ans.add(coordinate)
//            }
//            coodList.add(ans.toList())
//        }
//        return coodList.toList()
//    }

    fun setSnapshot(snap:Bitmap?){
        snapshot=snap
    }


    private fun uploadImage(bitmap: Bitmap,controller: NavController){
        Log.i("from tracker viewmodel","uploading initiated")
           val requestId = repo.uploadMapImage(bitmap,object : UploadCallback {
                override fun onStart(requestId: String?) {
                    Log.i("from tracker viewmodel","uploading started")
                    uploadingLoading.value=true
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    uploadingLoading.value=false
                    Log.i("from tracker view model","uploading result -> $resultData")
                    val list=LocationService.pathPoints.value!!
                    var timePassed=0L
                    if(list.size > 0 && list.last().size>0){
                        timePassed=list.last().last().time-startTime
                    }
                    val newRun = Run(
                        resultData?.get("secure_url").toString(),
                        startTime,
                        runInfo.value!!.distance,
                        timePassed,
                        runInfo.value!!.caloriesBurned,
                        MathUtils.computeAverageSpeed(LocationService.pathPoints.value!!),
                        MathUtils.computeMaxSpeed(LocationService.pathPoints.value!!),
                        ""
                    )
                    val bundle = Bundle()
                    bundle.putParcelable("run",newRun)
                    controller.navigate(R.id.action_trackerScreen_to_endRunScreen,bundle)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    uploadingLoading.value=false
                    Log.i("from tracker view model","uploading failed -> $error")
                    val list=LocationService.pathPoints.value!!
                    var timePassed=0L
                    if(list.size > 0 && list.last().size>0){
                        timePassed=list.last().last().time-startTime
                    }
                    val newRun = Run(
                        null,
                        startTime,
                        runInfo.value!!.distance,
                        timePassed,
                        runInfo.value!!.caloriesBurned,
                        MathUtils.computeAverageSpeed(LocationService.pathPoints.value!!),
                        MathUtils.computeMaxSpeed(LocationService.pathPoints.value!!),
                        ""
                    )
                    val bundle = Bundle()
                    bundle.putParcelable("run",newRun)
                    controller.navigate(R.id.action_trackerScreen_to_endRunScreen,bundle)
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            })
            Log.i("from tracker view model","upload request id -> $requestId")
    }

}