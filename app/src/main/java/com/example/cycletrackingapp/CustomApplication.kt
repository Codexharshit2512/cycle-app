package com.example.cycletrackingapp

import android.app.Application
import android.util.Log
import com.cloudinary.android.MediaManager
import com.example.cycletrackingapp.Repository.MainRepository
import com.example.cycletrackingapp.database.CycleTrackingDatabase
import java.util.HashMap

class CustomApplication : Application() {
    private val database by lazy {
        CycleTrackingDatabase.getInstance(this)
    }

    val repo by lazy {
        MainRepository(database.getRunDao())
    }


    override fun onCreate() {
        super.onCreate()
        Log.i("from application","app started")
        val map = HashMap<String,String>()
        map.put("api_key","713412413611291")
        map.put("api_secret","yxSza2h_IGxX94adOygcFtpNsWQ")
        map.put("cloud_name", "dv9upuqs7")
        MediaManager.init(this,map)
    }
}