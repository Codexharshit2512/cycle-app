package com.example.cycletrackingapp.utils

import com.example.cycletrackingapp.models.Coordinate
import com.example.cycletrackingapp.services.PathPoints
import kotlin.math.pow

object MathUtils {
    fun roundDecimal(places:Int,num:Double):Double{
        val mul = 10.0
        val shift = mul.pow(places)
        return ((num * shift).toInt()*1.0)/shift
    }

    fun computeAverageSpeed(pointsList:PathPoints):Double{
        var size = 0
        var totalSpeed = 0.0
        for(list in pointsList){
            size+=list.size
            for(loc in list){
                totalSpeed+=loc.speed
            }
        }
        if(size==0) return 0.0
        return totalSpeed/size
    }


    fun computeMaxSpeed(pointsList:PathPoints):Double{
        var maxSpeed= 0.0
        for(list in pointsList){
            for(loc in list){
                maxSpeed = maxOf(maxSpeed,loc.speed.toDouble())
            }
        }
        return maxSpeed
    }

}