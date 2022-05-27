package com.example.cycletrackingapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cycletrackingapp.models.Run

@Dao
interface CycleRunsDao {
    @Query("SELECT * FROM cycle_runs")
    fun getAllRuns() : LiveData<List<Run>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRun(run:Run)

    @Query("DELETE FROM cycle_runs")
    suspend fun deleteAllRuns()

//    @Query("SELECT * FROM cycle_runs WHERE maxSpeed=(SELECT MAX(maxSpeed) FROM cycle_runs) LIMIT 1")
//    suspend fun getMaxSpeedRun()
//
//    @Query("SELECT * FROM cycle_runs WHERE distance=(SELECT MAX(distance) FROM cycle_runs) LIMIT 1")
//    suspend fun getMaxDistanceRun()
//
//    @Query("SELECT * FROM cycle_runs WHERE calories=(SELECT MAX(calories) FROM cycle_runs) LIMIT 1")
//    suspend fun getMaxCaloriesRun()
//
//    @Query("SELECT * FROM cycle_runs WHERE averageSpeed=(SELECT MIN(averageSpeed) FROM cycle_runs) LIMIT 1")
//    suspend fun getMinSpeedRun()
//
//    @Query("SELECT * FROM cycle_runs WHERE maxSpeed=(SELECT MAX(maxSpeed) FROM cycle_runs) LIMIT 1")
//    suspend fun getMinCaloriesRun()
}