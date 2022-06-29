package com.example.cycletrackingapp.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.cycletrackingapp.R
import com.example.cycletrackingapp.databinding.FragmentRunDetailsScreenBinding
import com.example.cycletrackingapp.models.Run
import com.example.cycletrackingapp.utils.DateTimeUtil

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RunDetailsScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class RunDetailsScreen : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding:FragmentRunDetailsScreenBinding


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
        binding= FragmentRunDetailsScreenBinding.inflate(inflater,container,false)
        val run = arguments?.getParcelable("run") as Run?
        updateUI(run)
        return binding.root
    }

    private fun updateUI(run: Run?){
        run?.let {
            binding.apply {
                setTrackImage(run)
                durationText.infoText=DateTimeUtil.extractTime(it.time)
                distanceText.infoText=getString(R.string.run_value,it.distance.toString(),"km")
                avgSpeedText.infoText=getString(R.string.run_value,it.averageSpeed.toString(),"km/h")
                maxSpeedText.infoText=getString(R.string.run_value,it.maxSpeed.toString(),"km/h")
                caloriesText.infoText=getString(R.string.run_value,it.calories.toString(),"kcal")
            }
        }
    }

    private fun setTrackImage(run:Run?){
        run?.let {
            Glide.with(requireContext()).load(it.previewImage).into(binding.trackImage)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("from run details","toolbar -> ${activity?.actionBar}")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RunDetailsScreen.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RunDetailsScreen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}