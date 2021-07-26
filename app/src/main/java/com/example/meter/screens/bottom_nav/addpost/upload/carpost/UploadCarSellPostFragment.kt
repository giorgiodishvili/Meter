package com.example.meter.screens.bottom_nav.addpost.upload.carpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.meter.adapter.carpost.CarPostRecyclerAdapter
import com.example.meter.adapter.communitypost.main.CommunityPostsRecyclerViewAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.UploadCarSellPostFragmentBinding
import kotlinx.coroutines.launch

class UploadCarSellPostFragment :
    BaseFragment<UploadCarSellPostFragmentBinding, UploadCarSellPostViewModel>(
        UploadCarSellPostFragmentBinding::inflate,
        UploadCarSellPostViewModel::class.java
    ) {


    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
    }



}