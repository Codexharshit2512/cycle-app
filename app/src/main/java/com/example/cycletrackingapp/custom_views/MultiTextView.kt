package com.example.cycletrackingapp.custom_views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import com.example.cycletrackingapp.R
import com.example.cycletrackingapp.databinding.MultiTextViewBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.AlignSelf
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import java.lang.Error


class MultiTextView(context: Context,attrs:AttributeSet) : FlexboxLayout(context,attrs) {
    private lateinit var typedArray:TypedArray
    private lateinit var binding: MultiTextViewBinding
    var infoText:String? = ""
    set(value) {setValueText(value)}
    init{
      try{
          typedArray=context.obtainStyledAttributes(attrs,R.styleable.MultiTextView,0,0)
          val inflater = LayoutInflater.from(context)
          binding=MultiTextViewBinding.inflate(inflater,this,true)
          initUI()
      }
      catch(e:Error){
          e.printStackTrace()
      }
    finally {
        typedArray.recycle()
    }
    }

    private fun initUI(){
        setUpOrientation()
        setUpOrder()
        setupText()
        setupColor()
        setupFontSize()
        setupFontStyle()
    }

    private fun setUpOrientation(){
        val direction = typedArray.getString(R.styleable.MultiTextView_flexDirection)
        direction?.let{ dir ->
            when(dir){
                FlexDirection.ROW.toString() -> {
                    binding.flexContainer.flexDirection=FlexDirection.ROW
                }
                FlexDirection.COLUMN.toString() -> {
                    binding.apply {
                        flexContainer.flexDirection=FlexDirection.COLUMN
                        flexContainer.alignItems=AlignItems.CENTER
                        val flexParams = binding.smallText.layoutParams as FlexboxLayout.LayoutParams
                        flexParams.alignSelf=AlignSelf.AUTO
                    }
                }
                else->{
                    binding.flexContainer.flexDirection=FlexDirection.ROW
                }
            }
        }
    }

    private fun setUpOrder(){
        val valueOrder = typedArray.getInteger(R.styleable.MultiTextView_valueOrder,1)
        val descOrder = typedArray.getInteger(R.styleable.MultiTextView_descOrder,1)
        val valueLayoutParams = binding.bigText.layoutParams as FlexboxLayout.LayoutParams
        val descLayoutParams = binding.smallText.layoutParams as FlexboxLayout.LayoutParams
        valueLayoutParams.order=valueOrder
        descLayoutParams.order=descOrder

    }

    private fun setupText(){
        val description = typedArray.getString(R.styleable.MultiTextView_description)
        val value=typedArray.getString(R.styleable.MultiTextView_valueText)
        binding.smallText.text = description
        infoText = value
    }

    private fun setupFontSize(){
        val descriptionSize = convertPxToSp(typedArray.getDimension(R.styleable.MultiTextView_descriptionSize,convertSpToPx(17f)))
        val valueSize = convertPxToSp(typedArray.getDimension(R.styleable.MultiTextView_valueSize,convertSpToPx(30f)))
        Log.i("from multi text","descsize ->$descriptionSize")
        Log.i("from multi text","valuesize -> ${convertPxToSp(valueSize)}")
        binding.smallText.setTextSize(TypedValue.COMPLEX_UNIT_SP,descriptionSize)
        binding.bigText.setTextSize(TypedValue.COMPLEX_UNIT_SP,valueSize)
//        binding.smallText.textSize = descriptionSize
//        binding.bigText.textSize = valueSize
    }

    private fun setupColor(){
        val defDesColor = resources.getColor(R.color.light_grey)
        val defValColor= resources.getColor(R.color.white)
        val descriptionColor = typedArray.getColor(R.styleable.MultiTextView_descriptionColor,defDesColor)
        val valueColor = typedArray.getColor(R.styleable.MultiTextView_valueColor,defValColor)
        binding.smallText.setTextColor(descriptionColor)
        binding.bigText.setTextColor(valueColor)
    }

    private fun setupFontStyle(){
        val descriptionStyle = typedArray.getString(R.styleable.MultiTextView_descriptionStyle)
        val valueStyle = typedArray.getString(R.styleable.MultiTextView_valueStyle)
        Log.i("from multi text","style - > $descriptionStyle")
        var descTypeface = Typeface.NORMAL
        var valueTypeface = Typeface.NORMAL
        descriptionStyle?.let{
           descTypeface=when(descriptionStyle){
               "0" ->    Typeface.BOLD
               "1" -> Typeface.ITALIC
               else -> Typeface.NORMAL
           }
        }
        valueStyle?.let{
            valueTypeface=when(valueStyle){
                "0" ->    Typeface.BOLD
                "1" -> Typeface.ITALIC
                else -> Typeface.NORMAL
            }
        }
        binding.smallText.setTypeface(null,descTypeface)
        binding.bigText.setTypeface(null,valueTypeface)
    }

    private fun convertPxToSp(size:Float):Float{
        val density = resources.displayMetrics.scaledDensity
        return size / density
    }

    private fun convertSpToPx(size:Float):Float{
        val density = resources.displayMetrics.scaledDensity
        return size*density
    }

    private fun setValueText(text:String?){
        binding.bigText.text=text
    }
}