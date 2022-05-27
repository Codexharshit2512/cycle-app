package com.example.cycletrackingapp.custom_views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.cycletrackingapp.R
import com.example.cycletrackingapp.databinding.DynamicButtonBinding

class DynamicButton(context: Context,private val attrs:AttributeSet):LinearLayout(context,attrs) {

    private lateinit var binding:DynamicButtonBinding
    private lateinit var typedArray:TypedArray
    var loading=false
    init{
        try{
            typedArray = context.obtainStyledAttributes(attrs,R.styleable.DynamicButton,0,0)
            val inflater = LayoutInflater.from(context)
            binding = DynamicButtonBinding.inflate(inflater,this,true)
            initUI()
        }
        catch (e:Error){
            e.printStackTrace()
        }
        finally {
            typedArray.recycle()
        }
    }

    private fun initUI(){
        loading = typedArray.getBoolean(R.styleable.DynamicButton_loading,false)
        setButtonLoading(loading)
      setTextProperties()
    }

    private fun setTextProperties(){
        val size = convertPxToSp(typedArray.getDimension(R.styleable.DynamicButton_android_textSize,convertSpToPx(17f)))
        val color = typedArray.getColor(R.styleable.DynamicButton_android_textColor,resources.getColor(R.color.white))
        val style = typedArray.getIndex(R.styleable.DynamicButton_android_textStyle)
        val text=typedArray.getString(R.styleable.DynamicButton_text)
        Log.i("from dynamic button","text - $text")
        setTextSize(size)
        setTextColor(color)
        setText(text)
    }

    fun setText(text:String?){
        binding.buttonText.text = text
    }

     fun setTextSize(size:Float){
        binding.buttonText.setTextSize(TypedValue.COMPLEX_UNIT_SP,size)
    }

     fun setTextColor(color:Int){
        binding.buttonText.setTextColor(color)
    }

//    fun setTextStyle(size:Float){
//
//    }

    fun setButtonLoading(value:Boolean){
       loading=value
       if(loading){
           binding.buttonText.visibility=View.GONE
           binding.buttonLoader.visibility = View.VISIBLE
       }
        else{
           binding.buttonText.visibility=View.VISIBLE
           binding.buttonLoader.visibility = View.GONE
       }
    }

    private fun convertPxToSp(size:Float):Float{
        val density = resources.displayMetrics.scaledDensity
        return size / density
    }

    private fun convertSpToPx(size:Float):Float{
        val density = resources.displayMetrics.scaledDensity
        return size*density
    }
}