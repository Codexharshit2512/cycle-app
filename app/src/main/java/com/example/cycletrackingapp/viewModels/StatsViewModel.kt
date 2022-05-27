package com.example.cycletrackingapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.cycletrackingapp.Repository.MainRepository
import com.example.cycletrackingapp.models.Run


class StatsViewModel(private val repo:MainRepository):ViewModel() {
    private val runs : LiveData<List<Run>> = repo.runs
    val totalDistance:LiveData<Double> = Transformations.map(runs){calculateDistance(it)}
    val totalAverageSpeed:LiveData<Double> = Transformations.map(runs){calculateAverageSpeed(it)}
    val maxSpeed:LiveData<Double> = Transformations.map(runs){calculateMaxSpeed(it)}
    val totalCalories:LiveData<Double> = Transformations.map(runs){calculateTotalCalories(it)}
    val totalTime:LiveData<Long> = Transformations.map(runs){calculateTotalTime(it)}

    private fun calculateDistance(data:List<Run>):Double{
        var sum = 0.0
        for(run in data){
            sum+=run.distance
        }
        return sum
    }

    private fun calculateMaxSpeed(data:List<Run>):Double{
        var max = 0.0
        for(run in data){
            max= maxOf(max,run.maxSpeed)
        }
        return max
    }


    private fun calculateAverageSpeed(data:List<Run>):Double{
        var sum = 0.0
        for(run in data){
            sum+=run.distance
        }
        return sum/(data.size*1.0)
    }

    private fun calculateTotalCalories(data:List<Run>):Double{
        var sum = 0.0
        for(run in data){
            sum+=run.calories
        }
        return sum
    }

    private fun calculateTotalTime(data:List<Run>):Long{
        var sum = 0L
        for(run in data){
            sum+=run.time
        }
        return sum
    }
}