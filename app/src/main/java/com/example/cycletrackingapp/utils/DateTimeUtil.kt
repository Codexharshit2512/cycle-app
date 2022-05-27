package com.example.cycletrackingapp.utils

import android.content.Context
import com.example.cycletrackingapp.R
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {
    fun extractDate(time:Long):String{
        val pattern= "dd/MM/yyyy"
        val date= Date(time)
        val sdf= SimpleDateFormat(pattern)
        return sdf.format(date)
    }

    fun extractTime(time:Long,context: Context):String{
        var rem = time
        val msInHour=1000*60*60
        val hours = rem / msInHour
        rem-= (hours * msInHour)
        val msInMin=1000*60
        val min = rem / msInMin
        rem -= (min * msInMin)
        val sec = rem / 1000
        val hs:String=if(hours>9) hours.toString()
        else "0$hours"
        val ms = if(min>9) min.toString()
        else "0$min"
        val ss= if(sec>9) sec.toString()
        else "0$sec"

        return context.resources.getString(R.string.stop_watch_time,hs,ms,ss)
    }
}