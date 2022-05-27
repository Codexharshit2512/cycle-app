package com.example.cycletrackingapp.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.cycletrackingapp.CustomApplication
import com.example.cycletrackingapp.R
import com.example.cycletrackingapp.custom_views.RecordCard
import com.example.cycletrackingapp.databinding.FragmentRecordScreenBinding
import com.example.cycletrackingapp.utils.Constant
import com.example.cycletrackingapp.viewModels.HistoryViewModel
import com.example.cycletrackingapp.viewModels.ModelFactory
import com.example.cycletrackingapp.viewModels.RecordsViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecordScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecordScreen : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel:RecordsViewModel
    private lateinit var binding:FragmentRecordScreenBinding
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
        binding= FragmentRecordScreenBinding.inflate(inflater,container,false)
        setupViewModel()
        initCardData()
        setCardClickListeners()
        return binding.root
    }


    private fun setupViewModel(){
        activity?.let{
            val cApp = it.application as CustomApplication
            val factory = ModelFactory(cApp.repo, Constant.RECORD_VIEWMODEL_CODE)
            viewModel = ViewModelProvider(this,factory).get(RecordsViewModel::class.java)
        }
    }

    private fun initCardData(){
       val c1 = binding.root.getChildAt(0) as RecordCard
        val c2 = binding.root.getChildAt(0) as RecordCard
        val c3 = binding.root.getChildAt(0) as RecordCard
        val c4 = binding.root.getChildAt(0) as RecordCard
        c1.setValueText(viewModel.maxDistanceRun?.distance.toString())
        c2.setValueText(viewModel.maxCaloriesRun?.calories.toString())
        c3.setValueText(viewModel.maxDurationRun?.time.toString())
        c4.setValueText(viewModel.maxSpeedRun?.maxSpeed.toString())
    }

    private fun setCardClickListeners(){
        binding.root.getChildAt(0).setOnClickListener {
            val bundle = Bundle()
            val run = viewModel.maxDistanceRun
            bundle.putParcelable("run",run)
            findNavController().navigate(R.id.action_recordScreen_to_runDetailsScreen)
        }

        binding.root.getChildAt(1).setOnClickListener {
            val bundle = Bundle()
            val run = viewModel.maxCaloriesRun
            bundle.putParcelable("run",run)
            findNavController().navigate(R.id.action_recordScreen_to_runDetailsScreen)
        }

        binding.root.getChildAt(2).setOnClickListener {
            val bundle = Bundle()
            val run = viewModel.maxDurationRun
            bundle.putParcelable("run",run)
            findNavController().navigate(R.id.action_recordScreen_to_runDetailsScreen)
        }

        binding.root.getChildAt(3).setOnClickListener {
            val bundle = Bundle()
            val run = viewModel.maxSpeedRun
            bundle.putParcelable("run",run)
            findNavController().navigate(R.id.action_recordScreen_to_runDetailsScreen)
        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecordScreen.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecordScreen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}