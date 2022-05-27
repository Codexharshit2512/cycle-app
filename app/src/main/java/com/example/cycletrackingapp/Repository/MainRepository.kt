package com.example.cycletrackingapp.Repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.room.Dao
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import com.example.cycletrackingapp.database.CycleRunsDao
import com.example.cycletrackingapp.models.Run
import com.example.cycletrackingapp.utils.ConverterUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainRepository(private val runDao: CycleRunsDao) {
    val runs:LiveData<List<Run>> = runDao.getAllRuns()

    suspend fun insertNewRun(run:Run){
        runDao.addRun(run)
    }

    suspend fun deleteRuns(){
        runDao.deleteAllRuns()
    }

    fun uploadMapImage(bitmap: Bitmap,callback: UploadCallback):String{
        val byteArray = ConverterUtils.convertBitmapToByteArray(bitmap)
        return MediaManager.get().upload(byteArray)
            .option("folder","cycletracking")
            .unsigned("cff9yjq0")
            .callback(callback)
            .dispatch()
    }
}