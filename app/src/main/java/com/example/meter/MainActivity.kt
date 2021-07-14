package com.example.meter

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavBarSetup()
    }

    private fun bottomNavBarSetup() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView = binding.navView
        val chipNavigation: ChipNavigationBar = binding.bottomNavBar

        chipNavigation.setOnItemSelectedListener { itemId ->
            navView.selectedItemId = itemId
        }

        NavigationUI.setupWithNavController(navView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> chipNavigation.setItemSelected(R.id.navigation_home)
                R.id.navigation_search -> chipNavigation.setItemSelected(R.id.navigation_search)
                R.id.navigation_favourites -> chipNavigation.setItemSelected(R.id.navigation_favourites)
                R.id.navigation_profile -> chipNavigation.setItemSelected(R.id.navigation_profile)
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

}