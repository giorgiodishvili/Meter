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
        setListeners()
        observe()
    }

    private fun setListeners() {
//        binding.nextbutton.setOnClickListener {
//            val sellCarPost = SellCarPostRequest(
//                null,
//                binding.location.text.toString(),
//                null,
//                null,
//                null,
//                bluetooth = null,
//                null,
//                null,
//                null,
//                null,
//                binding.cylinders.toString().toInt(),
//                null,
//                null,
//                binding.doors.text.toString(),
//                true,
//                3.4,
//                "benzin",
//                0,
//                "GREY",
//                "NAGDI TYAVI",
//                true,
//                "Toyota",
//                130000,
//                "Prius",
//                multiWheel = true,
//                navigation = true,
//                photoUrl = mutableListOf(),
//                price = 30000,
//                rearViewCamera = true,
//                releaseYear = 2008.toString(),
//                signalization = true,
//                startStopSystem = true,
//                tires = "3/4",
//                gadacemataKolofi = "automatic",
//                user = null,
//                VIN = "ASDASDASDASD",
//                wheelSide = "left",
//                cruiseControl = true,
//                luqi = true,
//                antiSlide = true,
//                seatMemory = true,
//                sanisleparebi = true,
//                techOverview = true,
//                hydravlick = true,
//            )
//            i("sellCarPost", "$sellCarPost")
//            viewModel.createSellPost(
//                firebaseAuthImpl.getUserId(),
//                sellCarPost
//            )
//        }
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