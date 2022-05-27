package com.example.cycletrackingapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.cycletrackingapp.R
import com.example.cycletrackingapp.Repository.MainRepository
import com.example.cycletrackingapp.models.Run
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FinalizeRunViewModel(private val repo:MainRepository):ViewModel() {
    private var runTitle=""
    var loading=false

    fun setTitle(title:String){
        runTitle=title
    }

    fun addRun(run:Run,controller: NavController){
        run.title=runTitle
        loading=true
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertNewRun(run)
            loading=false
            controller.navigate(R.id.action_endRunScreen_to_historyFragment)
        }
    }
}