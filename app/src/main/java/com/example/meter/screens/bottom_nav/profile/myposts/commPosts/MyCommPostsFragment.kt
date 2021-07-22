package com.example.meter.screens.bottom_nav.profile.myposts.commPosts

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.MyCommPostsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCommPostsFragment : BaseFragment<MyCommPostsFragmentBinding, MyCommPostsViewModel>(
    MyCommPostsFragmentBinding::inflate,
    MyCommPostsViewModel::class.java
) {
//    private lateinit var adapter: CommunityPostsRecyclerViewAdapter

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {
//        initRecycler()
//        viewModel
    }

//    private fun initRecycler() {
//        binding.recycler.layoutManager = LinearLayoutManager(
//            requireContext(),
//            LinearLayoutManager.VERTICAL, false
//        )
//        adapter = CommunityPostsRecyclerViewAdapter()
//        binding.recycler.adapter = adapter
//    }


}