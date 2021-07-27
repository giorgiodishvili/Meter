package com.example.meter.screens.bottom_nav.addpost.upload.carpost.secondpart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.meter.R

class UploadSecondPartFragment : Fragment() {

    companion object {
        fun newInstance() = UploadSecondPartFragment()
    }

    private lateinit var viewModel: UploadSecondPartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.upload_second_part_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UploadSecondPartViewModel::class.java)
        // TODO: Use the ViewModel
    }

}