package com.example.meter.screens.bottom_nav.market.singlepost

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.meter.adapter.communitypost.singlepost.SingleCommunityPostPhotoRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.SingleCarSellPostFragmentBinding
import com.example.meter.entity.sell.SellCarPost
import com.example.meter.extensions.loadImg
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import com.example.meter.utils.transformers.ZoomPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SingleCarSellPostFragment :
    BaseFragment<SingleCarSellPostFragmentBinding, SingleCarSellPostViewModel>(
        SingleCarSellPostFragmentBinding::inflate,
        SingleCarSellPostViewModel::class.java
    ) {
    private var postId: Long = -1L

    private lateinit var car: SellCarPost

    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl

    private var userId: String = ""

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        getDataFromBundle()
        makeInitialCalls()
        observe()
    }

    private fun getDataFromBundle() {
        postId = arguments?.getLong("postId", -1L)!!
    }

    fun observe() {
        viewModel.post.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> Log.i("debugee", "ERROR")
                Resource.Status.SUCCESS -> {
                    it!!.data?.let { it1 ->
                        setUpPost(it1)
                        car = it.data!!
                    }
                }
                Resource.Status.LOADING -> Log.i("debugee", "loading")
            }
        })

    }

    private fun setUpPost(data: SellCarPost) {
        data.user?.url?.let { binding.authorIV.loadImg(it) }
        binding.descriptionTB.text = data.description
        binding.name.text = data.user!!.name
        binding.phoneNumber.text = data.user.number

        if (data.photoUrl.isNotEmpty()) {
            binding.singlePostRecyclerPhoto.adapter =
                SingleCommunityPostPhotoRecyclerAdapter(data.photoUrl)
            binding.singlePostRecyclerPhoto.setPageTransformer(ZoomPageTransformer)
        }

    }

    private fun makeInitialCalls() {
        firebaseAuthImpl.getUserId()?.let {
            userId = firebaseAuthImpl.getUserId()!!
        }

        viewModel.getPost(postId)
    }

}