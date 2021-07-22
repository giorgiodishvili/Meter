package com.example.meter.screens.bottom_nav.profile.myposts.marketPosts

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.MyMarketPostsFragmentBinding

class MyMarketPostsFragment : BaseFragment<MyMarketPostsFragmentBinding, MyMarketPostsViewModel>(
    MyMarketPostsFragmentBinding::inflate,
    MyMarketPostsViewModel::class.java
) {
    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {

    }



}