package com.example.meter.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.example.meter.base.BaseFragment
import com.example.meter.R
import com.example.meter.databinding.HomeFragmentBinding
import com.example.meter.network.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate) {
    private val homeViewModel: HomeViewModel by viewModels()


    override fun start(inflater: LayoutInflater, container: ViewGroup?) {
        homeViewModel.getAllCategories()
        observe()

    }
//
//    override fun onResume() {
//        super.onResume()
//        observe()
//    }

    private fun observe(){
        homeViewModel.categories.observe(viewLifecycleOwner, {
            when(it.status){
                Resource.Status.SUCCESS -> {
                    val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item,it.data!!)
                    binding.companyACTV.setAdapter(arrayAdapter)
                }
                Resource.Status.ERROR -> {
                    Log.i("shjowDialog", it.toString())
                }
                else -> {}
            }
        })
    }

}