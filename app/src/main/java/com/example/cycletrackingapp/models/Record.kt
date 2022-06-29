package com.example.cycletrackingapp.models

import android.graphics.drawable.Drawable

data class Record(
    val icon:Int,
    val title:String,
    val unit:String,
    val value:String,
    val run:Run?
)