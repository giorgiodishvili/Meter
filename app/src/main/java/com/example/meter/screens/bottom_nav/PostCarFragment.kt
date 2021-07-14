package com.example.meter.screens.bottom_nav

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.PostCarFragmentBinding

class PostCarFragment : BaseFragment<PostCarFragmentBinding, PostCarViewModel>(
    PostCarFragmentBinding::inflate,
    PostCarViewModel::class.java
) {
    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {

    }

}