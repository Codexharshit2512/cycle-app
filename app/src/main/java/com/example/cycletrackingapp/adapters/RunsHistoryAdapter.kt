package com.example.cycletrackingapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cycletrackingapp.databinding.CycleRunViewBinding
import com.example.cycletrackingapp.models.Run
import com.example.cycletrackingapp.utils.DateTimeUtil

class RunsHistoryAdapter(private val context:Context,private var list:List<Run>?)
    : RecyclerView.Adapter<RunsHistoryAdapter.ViewHolder>() {

    class ViewHolder(val binding:CycleRunViewBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = CycleRunViewBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.apply {
            val data = list!![position]
            holder.binding.apply {
                runTitle.text=data.title
                runDistance.text=data.distance.toString()
                averageSpeed.text = data.averageSpeed.toString()
                runTimestamp.text = DateTimeUtil.extractDate(data.timestamp)
                runTime.text = DateTimeUtil.extractTime(data.time,context)
            }
        }
    }

    override fun getItemCount(): Int {
       if(list!=null) return list?.size!!
        return 0
    }

    fun updateList(newList:List<Run>){
        list=newList
    }

}