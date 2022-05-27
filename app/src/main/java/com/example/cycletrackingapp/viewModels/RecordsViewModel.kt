package com.example.cycletrackingapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.cycletrackingapp.Repository.MainRepository
import com.example.cycletrackingapp.models.Run

class RecordsViewModel(private val repo:MainRepository):ViewModel() {

    private var runs:LiveData<List<Run>> = repo.runs
    private var changed = Transformations.map(runs){computeIndividualRuns()}
    var maxDistanceRun:Run?=null
    var maxDurationRun:Run?=null
    var maxCaloriesRun:Run?=null
    var maxSpeedRun:Run?=null

    private fun computeIndividualRuns(){
        var maxDistance=Double.MIN_VALUE
        var maxDuration = Long.MIN_VALUE
        var maxCalories = Double.MIN_VALUE
        var maxSpeed = Double.MIN_VALUE
        runs.value?.let{
            for(run in it){
                if(run.maxSpeed>maxSpeed){
                    maxSpeedRun = run
                    maxSpeed=run.maxSpeed
                }
                if(run.calories>maxCalories){
                    maxCaloriesRun = run
                    maxCalories = run.calories
                }
                if(run.distance>maxDistance){
                    maxDistanceRun = run
                    maxDistance = run.distance
                }
                if(run.time>maxDuration){
                    maxDurationRun = run
                    maxDuration = run.time
                }
            }
        }
    }
}