package com.example.meter

import android.annotation.SuppressLint
import android.app.Dialog
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
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
import com.example.meter.extensions.*
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl
    private lateinit var dialogItem: Dialog


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
        networkConnectionCheck()

        tokenListener()
        listeners()
        bottomNavBarSetup()
    }

    private fun networkConnectionCheck() {
        val networkCallback: NetworkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {

            }

            override fun onLost(network: Network) {
                popDialog()
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                popDialog()

            }

            override fun onUnavailable() {
                popDialog()
            }

        }

        val connectivityManager =
            applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request: NetworkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            connectivityManager.registerNetworkCallback(request, networkCallback)
        }
    }

    @SuppressLint("LongLogTag")
    private fun bottomNavBarSetup() {

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
        if (destination.id == R.id.splashScreenFragment || destination.id == R.id.navigation_profile
            || destination.id == R.id.completeProfileFragment
            || destination.id == R.id.chatFragment
            || destination.id == R.id.main_auth
        )
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

    fun popDialog() {
        dialogItem = Dialog(this)
        dialogItem.showDialog(R.layout.dialog_item_nointenet)
        CoroutineScope(Dispatchers.Main).launch {
            delay(4200)
            dialogItem.cancel()
        }
        dialogItem.show()
    }


}