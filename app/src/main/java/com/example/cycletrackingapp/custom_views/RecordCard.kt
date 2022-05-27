package com.example.cycletrackingapp.custom_views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cycletrackingapp.R
import com.example.cycletrackingapp.databinding.RecordCardBinding
import java.lang.Error

class RecordCard(context:Context,private val attrs:AttributeSet):ConstraintLayout(context,attrs) {
    private lateinit var typedArray: TypedArray
    private lateinit var binding:RecordCardBinding
    var recordHeading=""
    var recordValue=""
    var recordSubtitle=""
    init{
        try{
            typedArray=context.obtainStyledAttributes(attrs, R.styleable.RecordCard,0,0)
            val inflater = LayoutInflater.from(context)
            binding= RecordCardBinding.inflate(inflater,this,true)
            initUI()
        }
        catch(e: Error){
            e.printStackTrace()
        }
        finally {
            typedArray.recycle()
        }
    }

    private fun initUI(){
        val icon = typedArray.getDrawable(R.styleable.RecordCard_iconDrawable)
        val heading = typedArray.getString(R.styleable.RecordCard_heading)
        val valueText = typedArray.getString(R.styleable.RecordCard_valueText)
        val subtitle = typedArray.getString(R.styleable.RecordCard_subText)
        setIcon(icon)
        setHeading(heading)
        setValueText(valueText)
        setSubtitle(subtitle)
    }

    fun setIcon(drawable:Drawable?){
        drawable?.let {
            binding.cardIcon.setImageDrawable(it)
        }
    }

    fun setHeading(text:String?){
        text?.let{
            binding.cardTitle.text=it
        }
    }

    fun setValueText(text:String?){
        text?.let{
            binding.value.text=it
        }
    }

    fun setSubtitle(text:String?){
        text?.let{
            binding.date.text=it
        }
    }

}