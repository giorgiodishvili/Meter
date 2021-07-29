package com.example.meter.screens.bottom_nav.market.singlepost

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.adapter.communitypost.singlepost.SingleCommunityPostPhotoRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.SingleCarSellPostFragmentBinding
import com.example.meter.entity.sell.SellCarPost
import com.example.meter.extensions.loadImg
import com.example.meter.extensions.show
import com.example.meter.extensions.toFormattedDate
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
    private lateinit var singleCommunityPostPhotoRecyclerAdapter: SingleCommunityPostPhotoRecyclerAdapter

    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl

    private var userId: String = ""

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        getDataFromBundle()
        makeInitialCalls()
        observe()
        setListeners()
    }

    private fun setListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.deletebutton.setOnClickListener {
            viewModel.deletePost(postId)
        }
    }

    private fun getDataFromBundle() {
        postId = arguments?.getLong("postId", -1L)!!
    }

    fun observe() {
        viewModel.post.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> {
                    popDialog(R.layout.dialog_item_error, R.id.errorMsg, "მოხვდა რაღაც შეცდომა")
                }
                Resource.Status.SUCCESS -> {
                    it!!.data?.let { it1 ->
                        setUpPost(it1)
                        car = it.data!!
                    }
                }
                Resource.Status.LOADING -> {

                }
            }
        })

        viewModel.deletePost.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> {
                    Log.i("deletePost", "$it")
                    binding.deletebutton.show()
                }
                Resource.Status.SUCCESS -> {
                    binding.deletebutton.show()
                    Log.i("deletePost", it.data.toString())
                    Toast.makeText(requireContext(), "POST DELETED", Toast.LENGTH_SHORT).show()
                    binding.root.findNavController()
                        .navigate(R.id.action_singleCarSellPostFragment_to_navigation_marketPosts)
                }
                Resource.Status.LOADING -> Log.i("deletePost", "loading")
            }
        })

    }

    private fun setUpPost(data: SellCarPost) {
        data.user?.url?.let { binding.authorIV.loadImg(it) }
        binding.descriptionTB.text = data.description
        binding.name.text = data.user!!.name
//        binding.phoneNumber.text = getString(R.string.phone_single_post, data.user.number)
        binding.textView4.text = data.createdData.toFormattedDate()
        binding.singleTitle.text = data.articleHeader
        binding.manufacturerTV.text =
            getString(R.string.manufacturer_single_post, data.manufacturer)
        binding.yearTV.text = getString(R.string.year_single_post, data.releaseYear)
        binding.modelTV.text = getString(R.string.model_single_post, data.model)
        binding.fuelTypeTV.text = getString(R.string.fuel_type_single_post, data.fuel_type)
        binding.mileageTV.text = getString(R.string.mileage_single_post, data.mileage.toString())
        binding.vinTV.text = getString(R.string.vin_single_post, data.vin)
        binding.transmissionTypeTV.text =
            getString(R.string.transmission_type_single_post, data.transmission_type)
        binding.wheelSideTV.text = getString(R.string.wheel_side_single_post, data.wheel_side)
        binding.cylindersTV.text =
            getString(R.string.cylinder_single_post, data.cylinder.toString())

        if (data.photoUrl.isNotEmpty()) {
            singleCommunityPostPhotoRecyclerAdapter =
                SingleCommunityPostPhotoRecyclerAdapter(data.photoUrl)
            binding.singlePostRecyclerPhoto.adapter =
                singleCommunityPostPhotoRecyclerAdapter
            binding.singlePostRecyclerPhoto.setPageTransformer(ZoomPageTransformer)
        }
        if (userId == data.user.id) {
            binding.deletebutton.show()
        }

    }

    private fun makeInitialCalls() {
        firebaseAuthImpl.getUserId()?.let {
            userId = firebaseAuthImpl.getUserId()!!

        }

        viewModel.getPost(postId)
    }

}