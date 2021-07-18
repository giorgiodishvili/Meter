package com.example.meter.screens.bottom_nav.home

import android.util.Log.i
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meter.adapter.LatestPostRecyclerViewAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.HomeFragmentBinding
import com.example.meter.entity.SellCarPost
import com.example.meter.extension.onRightDrawableClicked
import com.example.meter.network.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding, HomeViewModel>(HomeFragmentBinding::inflate,
    HomeViewModel::class.java) {
    private lateinit var adapter: LatestPostRecyclerViewAdapter
    private lateinit var manufacturersList: List<String>
    private lateinit var makeList: List<String>

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        makeInitialCalls()
        setSearchListeners()
    }

    private fun makeInitialCalls() {
        observe()
        viewModel.getLatestPosts()
    }

    private fun initRecycler(data: List<SellCarPost>?) {
        adapter = LatestPostRecyclerViewAdapter(data!!)
        binding.recentPostsRV.adapter = adapter
        binding.recentPostsRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
    }

    private fun observe() {
        viewModel.latestPosts.observe(viewLifecycleOwner, { resource ->
            when(resource.status){
                Resource.Status.SUCCESS -> {
                    initRecycler(resource.data)
                }
                Resource.Status.ERROR -> {
                    i("shjowDialog", resource.toString())
                }
                else -> {}
            }
        })
    }

    private fun setSearchListeners() {
        binding.searchBar.onRightDrawableClicked{
            performSearch(it.text.toString())
        }

        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(binding.searchBar.text.toString())
            }
            true
        }
    }

    private fun performSearch(query: String) {
        viewModel.searchCarsForSale(query)
    }

    //
    override fun onResume() {
        super.onResume()
//        homeViewModel.getAllCategories()
//        observe()
//        binding.companyACTV.setOnItemClickListener { _, _, position, _ ->
//            homeViewModel.getModelFromMake(manufacturersList[position])
//        }
    }

//    private fun observe(){
//        homeViewModel.categories.observe(viewLifecycleOwner, { resource ->
//            when(resource.status){
//                Resource.Status.SUCCESS -> {
//                    manufacturersList = resource.data?.map { it.name }?.toImmutableList()!!
//
//                    val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item,
//                        manufacturersList.toTypedArray()
//                    )
//                    i("arrayAdapter", resource.data.toString())
//                    binding.companyACTV.setAdapter(arrayAdapter)
//                    binding.companyACTV.threshold = 2
//                }
//                Resource.Status.ERROR -> {
//                    Log.i("shjowDialog", resource.toString())
//                }
//                else -> {}
//            }
//        })
//
//        homeViewModel.make.observe(viewLifecycleOwner, { resource ->
//            when(resource.status){
//                Resource.Status.SUCCESS -> {
//                    makeList =  resource.data?.Results!!.map { it.Model_Name }
//
//                    val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item,
//                        makeList.toTypedArray()
//                    )
//                    i("arrayAdapter", resource.data.toString())
////                    binding.companyACTV.setAdapter(arrayAdapter)
//                }
//                Resource.Status.ERROR -> {
//                    Log.i("shjowDialog", resource.toString())
//                }
//                else -> {}
//            }
//        }
//        )
//    }



}