package com.example.meter.screens.auth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.MainAuthFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

class MainAuthFragment : BaseFragment<MainAuthFragmentBinding, MainAuthViewModel>(
    MainAuthFragmentBinding::inflate,
    MainAuthViewModel::class.java
) {
    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {

    }

}