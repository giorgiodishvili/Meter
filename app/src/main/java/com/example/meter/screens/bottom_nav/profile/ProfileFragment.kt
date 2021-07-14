package com.example.meter.screens.bottom_nav.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.ProfileFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileViewModel>(
    ProfileFragmentBinding::inflate,
    ProfileViewModel::class.java
) {

    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mAuth.currentUser == null) {
            findNavController().navigate(R.id.action_navigation_profile_to_main_auth)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {
        binding.button.setOnClickListener {
            mAuth.signOut()
            findNavController().navigate(R.id.action_navigation_profile_to_main_auth)        }
    }


}