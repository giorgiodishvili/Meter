package com.example.meter.screens.bottom_nav.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.FavouritesFragmentBinding

class FavouritesFragment : BaseFragment<FavouritesFragmentBinding, FavouritesViewModel>(
    FavouritesFragmentBinding::inflate,
    FavouritesViewModel::class.java
) {

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {

    }


}