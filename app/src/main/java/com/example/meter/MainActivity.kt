package com.example.meter

import android.animation.Animator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.util.Log
import android.util.Log.d
import android.util.Log.i
import android.view.Gravity
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
import com.google.firebase.ktx.Firebase
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

        binding.lottieAnimation.playAnimation()
        setUpAnimation()
//        bottomNavBarSetup()
        listeners()
        notification()
    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", "ChannelNew", importance).apply {
                description = "descriptionText"
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun notification(){
        createNotificationChannel()
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("My notification")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(2, builder.build())
        }
    }
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
            i("setOnItemSelectedListener","${it.id}")

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
        if (destination.id == R.id.main_auth || destination.id == R.id.navigation_profile || destination.id == R.id.completeProfileFragment)
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

}