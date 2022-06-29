package com.example.cycletrackingapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.math.MathUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.cycletrackingapp.databinding.RecordCardBinding
import com.example.cycletrackingapp.listeners.RunClickListener
import com.example.cycletrackingapp.models.Record
import com.example.cycletrackingapp.utils.DateTimeUtil


class RecordAdapter(private val context: Context,private var list:List<Record>,val listener: RunClickListener): RecyclerView.Adapter<RecordAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RecordCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = RecordCardBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = list[position]
        holder.binding.apply {
            cardIcon.setImageResource(record.icon)
            cardTitle.text=record.title
            value.text=record.value
            valueUnit.text=record.unit
            date.text=DateTimeUtil.extractDate(record.run!!.time)
            root.setOnClickListener(object  : View.OnClickListener{
                override fun onClick(p0: View?) {
                    listener.onRecordClick(record.run)
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateDataList(updatedList:List<Record>){
        list=updatedList
    }
}