package com.example.cycletrackingapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cycletrackingapp.models.Run


@Database(entities = [Run::class],version = 1,exportSchema = false)
abstract class CycleTrackingDatabase:RoomDatabase() {

     abstract fun getRunDao() : CycleRunsDao

     companion object{
        private var instance:CycleTrackingDatabase?=null

         fun getInstance(context: Context):CycleTrackingDatabase{
            return synchronized(this){
                 if(instance==null){
                     val ins= Room.databaseBuilder(
                         context.applicationContext,
                         CycleTrackingDatabase::class.java,
                         "cycle_tracking_database"
                     ).build()
                     instance=ins
                 }
                 instance!!
             }
         }
     }
}