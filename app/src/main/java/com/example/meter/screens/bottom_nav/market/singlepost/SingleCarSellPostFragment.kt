package com.example.meter.screens.bottom_nav.market.singlepost

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.SingleCarSellPostFragmentBinding

class SingleCarSellPostFragment : BaseFragment<SingleCarSellPostFragmentBinding,SingleCarSellPostViewModel>(
    SingleCarSellPostFragmentBinding::inflate,
    SingleCarSellPostViewModel::class.java
) {
    private var postId: Long = -1L

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        getDataFromBundle()
    }

    private fun getDataFromBundle() {
        postId = arguments?.getLong("postId", -1L)!!
    }

}