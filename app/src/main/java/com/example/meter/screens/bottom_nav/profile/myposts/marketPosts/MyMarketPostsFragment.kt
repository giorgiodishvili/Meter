package com.example.meter.screens.bottom_nav.profile.myposts.marketPosts

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meter.adapter.MyMarketPostsRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.base.SharedViewModel
import com.example.meter.databinding.MyMarketPostsFragmentBinding
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyMarketPostsFragment : BaseFragment<MyMarketPostsFragmentBinding, MyMarketPostsViewModel>(
    MyMarketPostsFragmentBinding::inflate,
    MyMarketPostsViewModel::class.java
) {
    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: MyMarketPostsRecyclerAdapter


    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {
        val currentUid = firebaseAuthImpl.getUserId().toString()

        sharedViewModel.userId.observe(requireActivity(), { uid ->

            Log.d("uiduid", uid)
            if (uid != "none") {
                viewModel.getUserInfo(uid)
                observers(uid)
            } else {
                sharedViewModel.saveUserId(currentUid)
                viewModel.getUserInfo(currentUid)
                observers(currentUid)
            }
        })

//        uid = arguments?.getString("uid2").toString() ?: firebaseAuthImpl.getUserId().toString()

    }

    private fun initRecycler() {
        adapter =
            MyMarketPostsRecyclerAdapter()
        binding.recycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.recycler.adapter = adapter
        adapter.onCardViewClick = { postId ->
            val bundle = bundleOf("postId" to postId)
//            binding.root.findNavController().navigate(
//                R.id.action_navigation_community_to_singleCommunityPostFragment,
//                bundle
//            )
        }

    }

    private fun observers(uid: String) {

        viewModel.readUserPosts.observe(this, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let { posts -> adapter.fetchPosts(posts.toMutableList()) }
                }
                Resource.Status.ERROR -> {
                }
                Resource.Status.LOADING -> {
                }
            }
        })

        viewModel.readUserInfo.observe(this, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    viewModel.getUserPosts(uid)
                    initRecycler()
                    adapter.getUserInfo(it.data)
                }
                Resource.Status.ERROR -> {
                }
                Resource.Status.LOADING -> {
                }
            }
        })
    }


}