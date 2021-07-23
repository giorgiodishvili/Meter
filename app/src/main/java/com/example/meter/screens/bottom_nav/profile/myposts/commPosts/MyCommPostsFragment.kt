package com.example.meter.screens.bottom_nav.profile.myposts.commPosts

import android.util.Log.d
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meter.adapter.MyCommPostsRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.MyCommPostsFragmentBinding
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyCommPostsFragment : BaseFragment<MyCommPostsFragmentBinding, MyCommPostsViewModel>(
    MyCommPostsFragmentBinding::inflate,
    MyCommPostsViewModel::class.java
) {
    private lateinit var adapter: MyCommPostsRecyclerAdapter
    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {
        d("checkBundle", "${arguments?.getString("uid")}")
        viewModel.getUserInfo("5")


        observers()
    }

    private fun initRecycler() {

        var userId = firebaseAuthImpl.getUserId()

        if (userId.isNullOrEmpty()) {
            userId = ""
        }

        adapter =
            MyCommPostsRecyclerAdapter(userId) { _, content, b ->
//                if (b) {
//                    viewModel.createLike(content.id, userId)
//                } else {
//                    viewModel.dislikePost(content.id, userId)
//                }
            }
        binding.recycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.recycler.adapter = adapter

    }
    private fun observers() {
        viewModel.readUserPosts.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let { posts -> adapter.fetchPosts(posts.toMutableList()) }
                }
                Resource.Status.ERROR -> {}
                Resource.Status.LOADING -> {}
            }
        })

        viewModel.readUserInfo.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    viewModel.getUserPosts("5")
                    initRecycler()
                    adapter.getUserInfo(it.data)
                }
                Resource.Status.ERROR -> {}
                Resource.Status.LOADING -> {}
            }
        })
    }


}