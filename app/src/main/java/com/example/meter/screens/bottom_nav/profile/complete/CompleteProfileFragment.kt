package com.example.meter.screens.bottom_nav.profile.complete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.CompleteProfileFragmentBinding
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import com.example.shualeduri.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CompleteProfileFragment : BaseFragment<CompleteProfileFragmentBinding, CompleteProfileViewModel>(
    CompleteProfileFragmentBinding::inflate,
    CompleteProfileViewModel::class.java
) {

    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (firebaseAuthImpl.getUserId() == null) {
            findNavController().navigate(R.id.action_navigation_profile_to_main_auth)
        }
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {
        viewModel.getUserInfo()
        listeners()
        observers()
    }

    private fun observers() {
        viewModel.readUserInfo.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    val name = it.data?.name
                    val arr = name?.split(" ".toRegex(), 2)?.toTypedArray()
                    binding.nameTv.text = arr?.get(0)
                    binding.surnameTv.text = arr?.get(1)
                }
                Resource.Status.ERROR -> {
                    requireContext().showToast("error loading user info")
                }
                Resource.Status.LOADING -> {}
            }
        })
    }

    private fun listeners() {
        binding.editProfileButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_completeProfileInfo)
        }

        binding.savedpostsButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_favourites)
        }

        binding.mypostsButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_myPostskFragment)
        }

        binding.logOutButton.setOnClickListener {
            firebaseAuthImpl.signOut()
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_home)

        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_home)
        }

    }

}