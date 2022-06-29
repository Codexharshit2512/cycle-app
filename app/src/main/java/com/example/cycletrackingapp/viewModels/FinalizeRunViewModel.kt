package com.example.cycletrackingapp.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.cycletrackingapp.R
import com.example.cycletrackingapp.Repository.MainRepository
import com.example.cycletrackingapp.models.Run
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FinalizeRunViewModel(private val repo:MainRepository):ViewModel() {

    var loading:MutableLiveData<Boolean> = MutableLiveData(false)

    fun addRun(run:Run,controller: NavController,title:String){
        run.title=title
        loading.value=true
        Log.i("from en view model","reached end")
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertNewRun(run)
            controller.navigate(R.id.action_endRunScreen_to_historyFragment)
        }
    }


}