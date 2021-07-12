package com.example.meter.ui.home

import android.util.Log
import android.util.Log.i
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.HomeFragmentBinding
import com.example.meter.network.Resource
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.toImmutableList

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate) {
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var manufacturersList: List<String>
    private lateinit var makeList: List<String>

    override fun start(inflater: LayoutInflater, container: ViewGroup?) {
    }
//
    override fun onResume() {
        super.onResume()
        homeViewModel.getAllCategories()
        observe()
    binding.companyACTV.setOnItemClickListener { _, _, position, _ ->
        homeViewModel.getModelFromMake(manufacturersList[position])
    }
    }

    private fun observe(){
        homeViewModel.categories.observe(viewLifecycleOwner, { resource ->
            when(resource.status){
                Resource.Status.SUCCESS -> {
                    manufacturersList = resource.data?.map { it.name }?.toImmutableList()!!

                    val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item,
                        manufacturersList.toTypedArray()
                    )
                    i("arrayAdapter", resource.data.toString())
                    binding.companyACTV.setAdapter(arrayAdapter)
                }
                Resource.Status.ERROR -> {
                    Log.i("shjowDialog", resource.toString())
                }
                else -> {}
            }
        })

        homeViewModel.make.observe(viewLifecycleOwner, { resource ->
            when(resource.status){
                Resource.Status.SUCCESS -> {
                    makeList =  resource.data?.Results!!.map { it.Model_Name }

                    val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item,
                        makeList.toTypedArray()
                    )
                    i("arrayAdapter", resource.data.toString())
//                    binding.companyACTV.setAdapter(arrayAdapter)
                }
                Resource.Status.ERROR -> {
                    Log.i("shjowDialog", resource.toString())
                }
                else -> {}
            }
        }
        )
    }



}