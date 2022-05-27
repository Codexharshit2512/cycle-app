package com.example.cycletrackingapp.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cycletrackingapp.CustomApplication
import com.example.cycletrackingapp.R
import com.example.cycletrackingapp.adapters.RunsHistoryAdapter
import com.example.cycletrackingapp.databinding.FragmentHistoryBinding
import com.example.cycletrackingapp.models.Run
import com.example.cycletrackingapp.utils.Constant
import com.example.cycletrackingapp.viewModels.HistoryViewModel
import com.example.cycletrackingapp.viewModels.ModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding:FragmentHistoryBinding
    private lateinit var viewModel : HistoryViewModel
    private lateinit var historyAdapter: RunsHistoryAdapter
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
        Log.i("from history","$container")
        binding= FragmentHistoryBinding.inflate(inflater,container,false)
        initUI()
        setupViewModel()
        return binding.root
    }

    private fun initUI(){
        setupViewModel()
        setActionButtonListener()
        setSpinnerAdapter()
        setupRunHistoryAdapter()
        addUIObservers()
    }

    private fun setActionButtonListener(){
        binding.floatActionBtn.setOnClickListener{
//            findNavController().navigate(R.id.action_historyFragment_to_trackerScreen)
            findNavController().navigate(R.id.action_historyFragment_to_runDetailsScreen)
//            viewModel.addNewRun(Run())
//            Toast.makeText(requireContext(),"Run added",Toast.LENGTH_LONG)
        }

        binding.testDelBtn.setOnClickListener {
            viewModel.deleteAllRuns()
            Toast.makeText(requireContext(),"Runs deleted",Toast.LENGTH_LONG)
        }
    }

    private fun setSpinnerAdapter(){
        val listener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, id: Long) {
                parent?.let{
                    val choice = parent.getItemAtPosition(pos)
                    Toast.makeText(requireContext(),"$choice",Toast.LENGTH_LONG)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sorting_choices,
            R.layout.custom_spinner_item
        ).also {
            binding.sortSpinner.apply {
                adapter=it
                onItemSelectedListener=listener
            }
        }
    }

    private fun setupRunHistoryAdapter(){
        historyAdapter = RunsHistoryAdapter(requireContext(),viewModel.runs.value)
        binding.runsHistoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter=historyAdapter
        }
    }

    private fun setupViewModel(){
        activity?.let{
            val cApp = it.application as CustomApplication
            val factory = ModelFactory(cApp.repo,Constant.HISTORY_VIEWMODEL_CODE)
            viewModel = ViewModelProvider(this,factory).get(HistoryViewModel::class.java)
        }
    }

    private fun addUIObservers(){
        viewModel.runs.observe(viewLifecycleOwner, Observer {
            historyAdapter.updateList(it)
            historyAdapter.notifyDataSetChanged()
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}