package com.example.cycletrackingapp.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cycletrackingapp.CustomApplication
import com.example.cycletrackingapp.R
import com.example.cycletrackingapp.adapters.RecordAdapter
import com.example.cycletrackingapp.custom_views.RecordCard
import com.example.cycletrackingapp.databinding.FragmentRecordScreenBinding
import com.example.cycletrackingapp.listeners.RunClickListener
import com.example.cycletrackingapp.models.Run
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
class RecordScreen : Fragment() ,RunClickListener{
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
        setUpRecylerView()
        return binding.root
    }


    private fun setupViewModel(){
        activity?.let{
            val cApp = it.application as CustomApplication
            val factory = ModelFactory(cApp.repo, Constant.RECORD_VIEWMODEL_CODE)
            viewModel = ViewModelProvider(this,factory).get(RecordsViewModel::class.java)
        }
    }

    private fun setUpRecylerView(){
        val recordsAdapter = RecordAdapter(requireContext(),viewModel.records.value!!,this)
        binding.recordRecyclerView.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=recordsAdapter
        }

        viewModel.records.observe(viewLifecycleOwner, Observer {
            recordsAdapter.updateDataList(it)
            recordsAdapter.notifyDataSetChanged()
        })
    }

    override fun onRecordClick(run: Run) {
        val bundle = Bundle()
        bundle.putParcelable("run",run)
        findNavController().navigate(R.id.action_recordScreen_to_runDetailsScreen,bundle)
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