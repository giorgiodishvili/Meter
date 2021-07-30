package com.example.meter.screens.bottom_nav.addpost.upload.carpost.firstpart

import android.util.Log.i
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.UploadCarSellPostFragmentBinding
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UploadCarSellPostFragment :
    BaseFragment<UploadCarSellPostFragmentBinding, UploadCarSellPostViewModel>(
        UploadCarSellPostFragmentBinding::inflate,
        UploadCarSellPostViewModel::class.java
    ) {

    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        observe()
    }

    private fun observe() {
        viewModel.sellPost.observe(viewLifecycleOwner, { it ->
            when (it.status) {
                Resource.Status.ERROR -> i("sellPost", "$it")
                Resource.Status.SUCCESS -> i("sellPost sucess", "${it.data}")
                Resource.Status.LOADING -> i("sellPost", "loading")
            }
        })
    }

}