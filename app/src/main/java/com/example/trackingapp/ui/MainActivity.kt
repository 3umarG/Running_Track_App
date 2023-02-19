package com.example.trackingapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.trackingapp.R
import com.example.trackingapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.rootView)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        setSupportActionBar(binding.toolbar)
        binding.bottomNavigationView.setupWithNavController(navController)

        // To Control the Visibility of the BottomNavigationView on certain Fragments
        navController
            .addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id){
                    R.id.settingsFragment , R.id.runFragment , R.id.statisticsFragment -> {
                        binding.bottomNavigationView.visibility = View.VISIBLE
                    }
                    else -> binding.bottomNavigationView.visibility = View.GONE
                }

            }
    }
}