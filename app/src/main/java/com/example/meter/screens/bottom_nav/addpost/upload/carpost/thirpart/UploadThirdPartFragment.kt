package com.example.meter.screens.bottom_nav.addpost.upload.carpost.thirpart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.meter.R

class UploadThirdPartFragment : Fragment() {

    companion object {
        fun newInstance() = UploadThirdPartFragment()
    }

    private lateinit var viewModel: UploadThirdPartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.upload_third_part_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UploadThirdPartViewModel::class.java)
        // TODO: Use the ViewModel
    }

}