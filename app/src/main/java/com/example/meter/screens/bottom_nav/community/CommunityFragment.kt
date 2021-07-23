package com.example.meter.screens.bottom_nav.community

import android.util.Log.d
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meter.R
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
        viewModel.getCommunityPosts()
        observe()

    }

    private fun initRecycler() {
        binding.recentPostsRV.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        adapter = CommunityPostsRecyclerViewAdapter()
        binding.recentPostsRV.adapter = adapter


        adapter.onProfilePicClick = { uid ->
            d("tagtag2", "navigate")
            findNavController().navigate(R.id.action_navigation_community_to_navigation_profile, bundleOf("uid" to uid))

        }
    }

    private fun observe() {
        viewModel.getCommunityPosts().observe(viewLifecycleOwner, { resource ->
            d("tagtag", "123")
            lifecycleScope.launch {
                adapter.submitData(resource)
            }
        })
    }

}