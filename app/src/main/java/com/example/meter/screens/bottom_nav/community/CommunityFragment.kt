package com.example.meter.screens.bottom_nav.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meter.adapter.CommunityPostsRecyclerViewAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.CommunityFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommunityFragment : BaseFragment<CommunityFragmentBinding, CommunityViewModel>(
    CommunityFragmentBinding::inflate,
    CommunityViewModel::class.java
) {

    private lateinit var adapter: CommunityPostsRecyclerViewAdapter

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        makeInitialCalls()
    }

    private fun makeInitialCalls() {
        initRecycler()
        observe()
        viewModel.getCommunityPosts()
    }

    private fun initRecycler() {
        binding.recentPostsRV.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        adapter = CommunityPostsRecyclerViewAdapter()
        binding.recentPostsRV.adapter = adapter
    }

    private fun observe() {
        viewModel.getCommunityPosts().observe(viewLifecycleOwner, { resource ->
            lifecycleScope.launch {
                adapter.submitData(resource)
            }
        })
    }

}