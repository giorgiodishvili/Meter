package com.example.meter.screens.bottom_nav

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.meter.R

class PostCarFragment : Fragment() {

    companion object {
        fun newInstance() = PostCarFragment()
    }

    private lateinit var viewModel: PostCarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.post_car_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PostCarViewModel::class.java)
        // TODO: Use the ViewModel
    }

}