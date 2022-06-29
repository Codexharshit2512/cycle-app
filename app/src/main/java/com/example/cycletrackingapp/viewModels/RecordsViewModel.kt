package com.example.cycletrackingapp.viewModels

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.cycletrackingapp.R
import com.example.cycletrackingapp.Repository.MainRepository
import com.example.cycletrackingapp.models.Record
import com.example.cycletrackingapp.models.Run
import com.example.cycletrackingapp.utils.DateTimeUtil

class RecordsViewModel(private val repo:MainRepository):ViewModel() {

    private var runs:LiveData<List<Run>> = repo.runs
    private var changed = Transformations.map(runs){computeIndividualRuns()}
    val records: MutableLiveData<List<Record>> = MutableLiveData(listOf())
    private var maxDistanceRun:Run?=null
    private var maxDurationRun:Run?=null
    private var maxCaloriesRun:Run?=null
    private var maxSpeedRun:Run?=null

    private fun computeIndividualRuns(){
        var maxDistance=Double.MIN_VALUE
        var maxDuration = Long.MIN_VALUE
        var maxCalories = Double.MIN_VALUE
        var maxSpeed = Double.MIN_VALUE
        runs.value?.let {
            for (run in it) {
                if (run.maxSpeed > maxSpeed) {
                    maxSpeedRun = run
                    maxSpeed = run.maxSpeed
                }
                if (run.calories > maxCalories) {
                    maxCaloriesRun = run
                    maxCalories = run.calories
                }
                if (run.distance > maxDistance) {
                    maxDistanceRun = run
                    maxDistance = run.distance
                }
                if (run.time > maxDuration) {
                    maxDurationRun = run
                    maxDuration = run.time
                }
            }
            createRecordList()
        }
    }

    private fun createRecordList(){
        val r1=Record(
            R.drawable.outline_directions_run_24,
            Resources.getSystem().getString(R.string.max_dist),
            "km",
            maxDistanceRun!!.distance.toString(),
            maxDistanceRun
        )
        val r2=Record(
            R.drawable.ic_baseline_local_fire_department_24,
            Resources.getSystem().getString(R.string.max_cal),
            "kcal",
            maxCaloriesRun!!.calories.toString(),
            maxCaloriesRun
        )
        val r3=Record(
            R.drawable.ic_baseline_watch_24,
            Resources.getSystem().getString(R.string.max_dist),
            "",
            DateTimeUtil.extractTime(maxDurationRun!!.time),
            maxDurationRun
        )
        val r4=Record(
            R.drawable.ic_baseline_speed_24,
            Resources.getSystem().getString(R.string.max_speed),
            "km/h",
            maxSpeedRun!!.maxSpeed.toString(),
            maxSpeedRun
        )
        records.value= listOf(r1,r2,r3,r4)
    }
}