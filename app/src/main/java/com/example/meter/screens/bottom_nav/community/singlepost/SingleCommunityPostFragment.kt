package com.example.meter.screens.bottom_nav.community.singlepost

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.meter.R

class SingleCommunityPostFragment : Fragment() {

    companion object {
        fun newInstance() = SingleCommunityPostFragment()
    }

    private lateinit var viewModel: SingleCommunityPostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.single_community_post_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SingleCommunityPostViewModel::class.java)
        // TODO: Use the ViewModel
    }

}