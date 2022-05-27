package com.example.cycletrackingapp.utils

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

object ConverterUtils {
    fun convertBitmapToByteArray(bitmap: Bitmap):ByteArray{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream)
        return stream.toByteArray()
    }
}