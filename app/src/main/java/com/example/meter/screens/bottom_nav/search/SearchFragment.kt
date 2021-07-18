package com.example.meter.screens.bottom_nav.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meter.adapter.CommunityPostsRecyclerViewAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.SearchFragmentBinding
import com.example.meter.entity.community.post.Content
import com.example.meter.network.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchFragmentBinding, SearchViewModel>(
    SearchFragmentBinding::inflate,
    SearchViewModel::class.java
) {

    private lateinit var adapter: CommunityPostsRecyclerViewAdapter

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        makeInitialCalls()
    }

    private fun makeInitialCalls() {
        observe()
        viewModel.getCommunityPosts()
    }

    private fun initRecycler(data: List<Content>) {
        adapter = CommunityPostsRecyclerViewAdapter(data)
        binding.recentPostsRV.adapter = adapter
        binding.recentPostsRV.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)
    }

    private fun observe() {
        viewModel.latestPosts.observe(viewLifecycleOwner, { resource ->
            when(resource.status){
                Resource.Status.SUCCESS -> {
                    resource.data?.let { initRecycler(it.content) }
                }
                Resource.Status.ERROR -> {
                    Log.i("shjowDialog", resource.toString())
                }
                else -> {}
            }
        })
    }


}