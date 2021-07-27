package com.example.meter.screens.bottom_nav.addpost.upload.carpost.fourthpart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.meter.R

class UploadFourthPartFragment : Fragment() {

    companion object {
        fun newInstance() = UploadFourthPartFragment()
    }

    private lateinit var viewModel: UploadFourthPartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.upload_fourth_part_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UploadFourthPartViewModel::class.java)
        // TODO: Use the ViewModel
    }

}