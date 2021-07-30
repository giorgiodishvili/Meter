package com.example.meter.screens.bottom_nav.addpost.upload.carpost

import android.util.Log.d
import android.util.Log.i
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meter.R
import com.example.meter.adapter.carpost.upload.ManufacturerRecyclerAdapter
import com.example.meter.adapter.carpost.upload.ModelRecylcer
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.UploadCarSellPostFragmentBinding
import com.example.meter.entity.sell.SellCarPostRequest
import com.example.meter.extensions.setGone
import com.example.meter.extensions.show
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

    private lateinit var wheelSide: String
    private lateinit var manufacturer: String
    private lateinit var model: String

    private lateinit var adapterManufact: ManufacturerRecyclerAdapter
    private lateinit var adapterModel: ModelRecylcer

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        getData()
        observe()
        setListeners()
        initManufactAdapter()
    }

    private fun initManufactAdapter() {
        adapterManufact = ManufacturerRecyclerAdapter()
        binding.manufacturerRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.manufacturerRecycler.adapter = adapterManufact
        viewModel.getManufacturers()
    }

    private fun initModelAdapter() {
        adapterModel = ModelRecylcer()
        binding.modelRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.modelRecycler.adapter = adapterModel
    }

    private fun getData() {
        binding.carModelChooser.isClickable = false
        binding.leftwheel.setOnClickListener {
            wheelSide = "left"
            it.setBackgroundResource(R.drawable.card_button_shape_selected)
            binding.rightwheel.setBackgroundResource(R.drawable.card_button_shape)
        }
        binding.rightwheel.setOnClickListener {
            wheelSide = "right"
            it.setBackgroundResource(R.drawable.card_button_shape_selected)
            binding.leftwheel.setBackgroundResource(R.drawable.card_button_shape)

        }
//        adapterManufact.addData()
    }


    private fun setListeners() {

        binding.nextbutton.setOnClickListener {
            val inputCheck =
                this::manufacturer.isInitialized && this::model.isInitialized && this::wheelSide.isInitialized && binding.engineCap.text.isNotEmpty()
                        && !binding.engineCap.text.startsWith(".") && !binding.engineCap.text.endsWith(
                    "."
                )
                        && binding.cylinders.text.isNotEmpty() && binding.priceET.text.isNotEmpty()

            if (inputCheck) {
                d("tagtagtag", "aadsf")

                val sellCarPost = SellCarPostRequest(
                    binding.location.text.toString(),
                    binding.cylinders.text.toString().toInt(),
                    null,
                    binding.engineCap.text.toString().toDouble(),
                    binding.fuelType.text.toString(),
                    0,
                    manufacturer,
                    model,
                    null,
                    binding.priceET.text.toString().toInt(),
                    binding.makeYearEt.text.toString(),
                    binding.transmision.text.toString(),
                    firebaseAuthImpl.getUserId(),
                    wheelSide,
                    binding.vintv.text.toString()
                )
                val bundle = bundleOf("sellPost" to sellCarPost)
                findNavController().navigate(
                    R.id.action_uploadCarSellPostFragment_to_photoUploadCarSellFragment,
                    bundle
                )
                i("sellCarPost", "$sellCarPost")

            }
        }

        binding.carManufacturerChooser.setOnClickListener {

            if (binding.manufacturerRecycler.isVisible) {
                binding.manufacturerRecycler.setGone()
            } else {
                binding.manufacturerRecycler.show()
            }
        }

        binding.carModelChooser.setOnClickListener {
            if (binding.modelRecycler.isVisible) {
                binding.modelRecycler.setGone()
            } else {
                binding.modelRecycler.show()
            }
        }

        binding.root.setOnClickListener {
            binding.manufacturerRecycler.setGone()
            binding.modelRecycler.setGone()
        }
    }

    private fun observe() {
        viewModel.getManufacturers.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let { it1 ->
                        adapterManufact.addData(it1.toMutableList())
                        initModelAdapter()
                        adapterManufact.onManufacturerClick = { manufacturer ->
                            binding.carManufacturerChooser.text = manufacturer
                            this.manufacturer = manufacturer
                            binding.manufacturerRecycler.setGone()
                            viewModel.getModel(manufacturer)
                        }
                    }
                }
                Resource.Status.ERROR -> i("sellPost", "$it")
                Resource.Status.LOADING -> i("sellPost", "loading")
            }
        })

        viewModel.getModel.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let { it1 ->
                        binding.carModelChooser.isClickable = true
                        adapterModel.addData(it.data.Results.toMutableList())
                        adapterModel.onModelClick = { model ->
                            binding.carModelChooser.text = model
                            this.model = model
                            binding.modelRecycler.setGone()
                        }
                    }
                }
                Resource.Status.ERROR -> i("sellPost", "$it")
                Resource.Status.LOADING -> i("sellPost", "loading")
            }
        })

        viewModel.sellPost.observe(viewLifecycleOwner, { it ->
            when (it.status) {
                Resource.Status.ERROR -> i("sellPost", "$it")
                Resource.Status.SUCCESS -> i("sellPost sucess", "${it.data}")
                Resource.Status.LOADING -> i("sellPost", "loading")
            }
        })
    }

}