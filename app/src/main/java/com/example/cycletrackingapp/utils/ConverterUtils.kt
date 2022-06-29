package com.example.cycletrackingapp.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.res.ResourcesCompat
import com.example.cycletrackingapp.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.io.ByteArrayOutputStream

object ConverterUtils {
    fun convertBitmapToByteArray(bitmap: Bitmap):ByteArray{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream)
        return stream.toByteArray()
    }

    fun convertVectorToBitmap(drawable:Int,context: Context):BitmapDescriptor{
        val vectorDrawable= ResourcesCompat.getDrawable(context.resources,drawable,null)
        val height=100
        val width=100
        vectorDrawable!!.setBounds(0,0,vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight)
        val bitmap=Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888)
        val canvas= Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}