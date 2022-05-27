package com.example.cycletrackingapp.services

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cycletrackingapp.utils.Constant
import com.example.cycletrackingapp.utils.LocationUpdateClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

typealias PathPoint=MutableList<Location>
typealias PathPoints=MutableList<PathPoint>

class LocationService: Service() {
    private var locationClient: FusedLocationProviderClient?=null
    private var locationClientProvider: LocationUpdateClient?=null
    private val TAG="LOCATION SERVICE"
    companion object{
        var pathPoints= MutableLiveData<PathPoints>().also {
            it.value= mutableListOf()
        }
        var tracking = MutableLiveData<String>().apply {
            this.value=Constant.TRACKING_NOT_STARTED
        }

    }

    private fun initializeData(){
        pathPoints.let{
            it.value= mutableListOf()
            it.value?.add(mutableListOf())
        }
        tracking.value=Constant.TRACKING_NOT_STARTED
    }

    private fun addLocationToList(location: Location){
        location ?: return
        pathPoints.value.apply{
            this?.last()?.add(location)
            pathPoints.postValue(this)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG,"location service created")
        initializeData()
        locationClientProvider= LocationUpdateClient()
        locationClient=locationClientProvider?.initializeLocationClient(this,locationCallback)
    }

    private val locationCallback= object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult ?: return
            addLocationToList(locationResult.lastLocation)
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let{
            when(intent.action){
                Constant.START_OR_RESUME_LOCATION_SERVICE -> {
                    startTracking()
                    locationClientProvider?.startLocationUpdates(this)
                    Log.i(TAG,"location service started")
                }
                Constant.STOP_LOCATION_SERVICE -> {
                    stopLocationService()
                    Log.i(TAG,"location service stopped")
                }
                Constant.PAUSE_LOCATION_SERVICE->{
                    pauseLocationService()
                    pathPoints.value?.add(mutableListOf())
                    Log.i(TAG,"location service paused")
                }
                else -> stopLocationService()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun stopTracking(){
        tracking.value=Constant.TRACKING_STOPPED
    }

    private fun startTracking(){
        tracking.value=Constant.TRACKING_IN_PROGRESS
    }

    private fun pauseTracking(){
        tracking.value=Constant.TRACKING_PAUSED
    }

    private fun stopLocationService(){
        locationClientProvider?.stopLocationUpdates()
        stopTracking()
        stopSelf()
    }

    private fun pauseLocationService(){
        locationClientProvider?.stopLocationUpdates()
        pauseTracking()
    }

    private fun resetCoordinates(){
        initializeData()
    }
}