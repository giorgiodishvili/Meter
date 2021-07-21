package com.example.meter

import android.os.Bundle
import android.util.Log.d
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.meter.databinding.ActivityMainBinding
import com.example.meter.extensions.setGone
import com.example.meter.extensions.show
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        bottomNavBarSetup()
    }

    private fun bottomNavBarSetup() {

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView = binding.navView
        val chipNavigation: ChipNavigationBar = binding.bottomNavBar

        chipNavigation.setOnItemSelectedListener { itemId ->
            navView.selectedItemId = itemId
        }

        NavigationUI.setupWithNavController(navView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    chipNavigation.setItemSelected(R.id.navigation_home)
                }
                R.id.navigation_favourites -> chipNavigation.setItemSelected(R.id.navigation_favourites)
                R.id.navigation_profile -> {
                    chipNavigation.setItemSelected(R.id.navigation_profile)
                }
                R.id.main_auth -> {
                    handleBackPressed(destination)
                }
            }
            hideIfAuth(destination, chipNavigation)

        }
    }

    private fun hideIfAuth(destination: NavDestination, navBar: ChipNavigationBar) {
        if (destination.id == R.id.main_auth)
            navBar.setGone()
        else
            navBar.show()
    }

    private fun handleBackPressed(destination: NavDestination) {
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (destination.id) {
                    R.id.main_auth -> {
                        d("loglog", "executed")
                        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                        navController.navController.navigate(R.id.action_global_navigation_home)
                    }
                    R.id.navigation_home -> {

                    }
                }
            }
        })
    }

}