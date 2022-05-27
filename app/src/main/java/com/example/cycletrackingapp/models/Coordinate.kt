package com.example.cycletrackingapp.models

data class Coordinate(
    val latitude:Double,
    val longitude:Double,
    val speed:Double,
    val distance:Double,
    val distanceFromLastPoint:Double,
    val altitude:Double,
    val time:Long
)
