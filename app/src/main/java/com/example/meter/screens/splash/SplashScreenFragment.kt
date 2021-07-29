package com.example.meter.screens.splash

import android.animation.Animator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.SplashScreenFragmentBinding
import com.example.meter.extensions.show
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import com.example.meter.repository.firebase.RealtimeDbRepImpl
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenFragment : BaseFragment<SplashScreenFragmentBinding, SplashScreenViewModel>(
    SplashScreenFragmentBinding::inflate,
    SplashScreenViewModel::class.java
) {
    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl
    @Inject
    lateinit var db: RealtimeDbRepImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDb()
    }

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        setUpAnimation()
    }

    private fun setUpAnimation() {
        Log.d("taglag", "animation")
        binding.lottieAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                findNavController().navigate(R.id.action_splashScreenFragment_to_navigation_community)
                requireActivity().findViewById<ChipNavigationBar>(R.id.bottom_nav_bar).show()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
    }

    private fun initDb() {
        db.incomingChat(firebaseAuthImpl.getUserId().toString())
    }


}