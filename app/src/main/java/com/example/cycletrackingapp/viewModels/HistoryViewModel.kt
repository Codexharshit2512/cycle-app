package com.example.cycletrackingapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cycletrackingapp.Repository.MainRepository
import com.example.cycletrackingapp.models.Run
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HistoryViewModel(private val repo:MainRepository):ViewModel() {
    val runs : LiveData<List<Run>> = repo.runs

    fun addNewRun(run: Run){
        viewModelScope.launch {
            repo.insertNewRun(run)
        }
    }

    fun deleteAllRuns(){
        viewModelScope.launch {
            repo.deleteRuns()
        }
    }
}