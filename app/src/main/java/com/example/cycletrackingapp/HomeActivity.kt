package com.example.cycletrackingapp

import android.app.ActionBar
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.cycletrackingapp.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater,null,false)
        setContentView(binding.root)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
        val navController = Navigation.findNavController(this,R.id.home_nav_fragment)
        bottomNav.setupWithNavController(navController)
        setupActionBar()
        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener{
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?,
            ) {
                Log.i("home act","${destination.label}")
                when(destination.id){
                    R.id.trackerScreen -> {
                        hideBottomNav()
                        binding.toolbarContainer.customToolbar.visibility=View.VISIBLE
                    }
                    R.id.endRunScreen -> {
                        hideBottomNav()
                        binding.toolbarContainer.customToolbar.visibility=View.VISIBLE
                    }
                    R.id.runDetailsScreen -> {
                        hideBottomNav()
                        binding.toolbarContainer.customToolbar.visibility=View.VISIBLE
                    }
                    else -> {
                        showBottomNav()
                        binding.toolbarContainer.customToolbar.visibility=View.GONE
                    }
                }
            }
        })
    }

    private fun setupActionBar(){
        Log.i("from home","toolbar -> ${supportActionBar}")
        supportActionBar?.hide()
//        setSupportActionBar(binding.toolbarContainer.customToolbar)
        val controller=findNavController(R.id.home_nav_fragment)
        binding.toolbarContainer.customToolbar.setupWithNavController(
            controller,
            AppBarConfiguration(controller.graph)
        )
    }

    private fun showBottomNav(){
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
        bottomNav.visibility=View.VISIBLE
    }

    private fun hideBottomNav(){
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
        bottomNav.visibility=View.GONE
    }

    override fun onStart() {
        super.onStart()
        Log.i("from home act","action bar - $supportActionBar")
//        val colorDrawable = ColorDrawable(Color.parseColor("#00a693"))
//        supportActionBar?.setBackgroundDrawable(colorDrawable)
//        actionBar?.setBackgroundDrawable(colorDrawable)
    }

}