package com.example.meter.screens.bottom_nav.profile.myposts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.meter.R

class MyPostskFragment : Fragment() {

    companion object {
        fun newInstance() = MyPostskFragment()
    }

    private lateinit var viewModel: MyPostskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_postsk_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyPostskViewModel::class.java)
        // TODO: Use the ViewModel
    }

}