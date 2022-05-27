package com.example.cycletrackingapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cycletrackingapp.Repository.MainRepository
import com.example.cycletrackingapp.utils.Constant

class ModelFactory(private val repo:MainRepository?,private val type:Int):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(type==Constant.HISTORY_VIEWMODEL_CODE){
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repo!!) as T
        }
        else if(type==Constant.STATS_VIEWMODEL_CODE){
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(repo!!) as T
        }
        else if(type==Constant.TRACKER_VIEWMODEL_CODE){
            @Suppress("UNCHECKED_CAST")
            return TrackerViewModel(repo!!) as T
        }
        else if(type==Constant.RECORD_VIEWMODEL_CODE){
            @Suppress("UNCHECKED_CAST")
            return RecordsViewModel(repo!!) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}