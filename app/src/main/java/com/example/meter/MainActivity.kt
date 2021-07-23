package com.example.meter

import android.animation.Animator
import android.os.Bundle
import android.util.Log.d
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.meter.base.SharedViewModel
import com.example.meter.databinding.ActivityMainBinding
import com.example.meter.extensions.setGone
import com.example.meter.extensions.show
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    lateinit var chipNavigation: ChipNavigationBar
    private val sharedViewModel: SharedViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lottieAnimation.playAnimation()
        setUpAnimation()
    }

    private fun bottomNavBarSetup() {
        d("tagtag", "bottomnav")
        binding.lottieAnimation.setGone()

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val navView = binding.navView
        chipNavigation = binding.bottomNavBar

        chipNavigation.setItemSelected(R.id.navigation_community)

        chipNavigation.setOnItemSelectedListener { itemId ->
            navView.selectedItemId = itemId
        }

        supportFragmentManager.beginTransaction()

        NavigationUI.setupWithNavController(navView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_community -> {
                    chipNavigation.setItemSelected(R.id.navigation_community)
                }
                R.id.navigation_profile -> {
                    sharedViewModel.saveUserId("none")
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
                        val navController =
                            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                        navController.navController.navigate(R.id.action_global_navigation_home)
                    }
                    R.id.navigation_community -> {

                    }
                }
            }
        })
    }

    private fun setUpAnimation() {
        d("taglag", "animation")
        binding.lottieAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                bottomNavBarSetup()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
    }

}