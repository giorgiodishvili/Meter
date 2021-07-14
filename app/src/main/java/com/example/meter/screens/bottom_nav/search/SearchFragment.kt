package com.example.meter.screens.bottom_nav.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.SearchFragmentBinding

class SearchFragment : BaseFragment<SearchFragmentBinding, SearchViewModel>(
    SearchFragmentBinding::inflate,
    SearchViewModel::class.java
) {

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {

    }


}