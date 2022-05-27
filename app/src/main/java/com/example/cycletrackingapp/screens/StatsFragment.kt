package com.example.cycletrackingapp.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.cycletrackingapp.CustomApplication
import com.example.cycletrackingapp.R
import com.example.cycletrackingapp.databinding.FragmentStatsBinding
import com.example.cycletrackingapp.utils.Constant
import com.example.cycletrackingapp.viewModels.ModelFactory
import com.example.cycletrackingapp.viewModels.StatsViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding:FragmentStatsBinding
    private lateinit var viewModel:StatsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentStatsBinding.inflate(inflater,container,false)
//        Log.i("from stats frag","roooot -> ${binding.totDist}")
        initViewModel()
        assignObservers()
        return binding.root
    }

    private fun initViewModel(){
        activity?.let{ activity ->
            val app = activity.application as CustomApplication
            val factory = ModelFactory(app.repo,Constant.STATS_VIEWMODEL_CODE)
            viewModel= ViewModelProvider(this,factory).get(StatsViewModel::class.java)
        }
    }


    private fun assignObservers(){

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}