package com.example.meter.screens.bottom_nav.addpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.PostCarFragmentBinding

class PostCarFragment : BaseFragment<PostCarFragmentBinding, PostCarViewModel>(
    PostCarFragmentBinding::inflate,
    PostCarViewModel::class.java
) {
    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        setListeners()
    }

    private fun setListeners() {
        binding.communityPost.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_post_to_uploadCommunityPostFragment)
        }

        binding.marketPost.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_post_to_uploadCarSellPostFragment)
        }
    }


}