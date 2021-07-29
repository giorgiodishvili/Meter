package com.example.meter

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.util.Log
import android.util.Log.d
import android.util.Log.i
import android.view.Gravity
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.meter.base.SharedViewModel
import com.example.meter.databinding.ActivityMainBinding
import com.example.meter.extensions.fade
import com.example.meter.extensions.setGone
import com.example.meter.extensions.show
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private val sharedViewModel: SharedViewModel by viewModels()

    companion object {
        const val ADD_POST = R.id.navigation_post
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenListener()
        binding.lottieAnimation.playAnimation()
        setUpAnimation()
//        bottomNavBarSetup()
        listeners()
    }

    @SuppressLint("LongLogTag")
    private fun bottomNavBarSetup() {

        binding.lottieAnimation.setGone()

        val navView = binding.navView
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val chipNavigation = binding.bottomNavBar
        chipNavigation.setItemSelected(R.id.navigation_community)

        chipNavigation.setOnItemSelectedListener { itemId ->
            d("tagtag", "$itemId")
            navView.selectedItemId = itemId
            if (itemId == ADD_POST) {
                if (firebaseAuthImpl.getUserId().isNullOrEmpty()) {
                    navController.navigate(R.id.navigation_profile)
                } else {
                    showButtons()
                }
            }
        }

        chipNavigation.setOnClickListener {
            i("setOnItemSelectedListener", "${it.id}")

        }

        supportFragmentManager.beginTransaction()
        NavigationUI.setupWithNavController(navView, navController)
        val slide: Transition = Slide(Gravity.END)
        slide.excludeTarget(navView, true)
        window.enterTransition = slide

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_community -> {
                    chipNavigation.setItemSelected(R.id.navigation_community)
                }
                R.id.navigation_marketPosts -> {
                    chipNavigation.setItemSelected(R.id.navigation_marketPosts)
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

    private fun listeners() {
        binding.sellButton.setOnClickListener {
            navController.navigate(R.id.action_global_uploadCarSellPostFragment)
        }
        binding.postButton.setOnClickListener {
            navController.navigate(R.id.action_global_uploadCommunityPostFragment)
        }
    }

    private fun hideIfAuth(destination: NavDestination, navBar: ChipNavigationBar) {
        hideButtons()
        if (destination.id == R.id.main_auth || destination.id == R.id.navigation_profile || destination.id == R.id.completeProfileFragment || destination.id == R.id.chatFragment)
            navBar.fade()
        else {
            navBar.show()
        }
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

    private fun showButtons() {
        binding.postButton.show()
        binding.sellButton.show()
    }

    private fun hideButtons() {
        binding.sellButton.setGone()
        binding.postButton.setGone()
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


    private fun tokenListener() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            token?.let {
                i("token", it)
                getSharedPreferences("_", MODE_PRIVATE).edit().putString("token", it).apply()
                sharedViewModel.saveOnlyToken(it)
            }

        })
    }
}