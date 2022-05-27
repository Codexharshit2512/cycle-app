package com.example.cycletrackingapp.custom_views

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.cycletrackingapp.R

class PresentationalDialog(private val resource:Int):DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            val builder = AlertDialog.Builder(it)
            builder.setView(resource)
            // Create the AlertDialog object and return it
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }
}