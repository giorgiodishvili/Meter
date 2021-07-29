package com.example.meter.screens.bottom_nav.community

import android.util.Log.d
import android.util.Log.i
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meter.R
import com.example.meter.adapter.communitypost.main.CommunityPostsRecyclerViewAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.CommunityFragmentBinding
import com.example.meter.entity.push_notification.PushNotificationResponse
import com.example.meter.extensions.loadProfileImg
import com.example.meter.extensions.setGone
import com.example.meter.network.Resource
import com.example.meter.paging.loadstate.LoaderStateAdapter
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CommunityFragment : BaseFragment<CommunityFragmentBinding, CommunityViewModel>(
    CommunityFragmentBinding::inflate,
    CommunityViewModel::class.java
) {
    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl

    private lateinit var adapter: CommunityPostsRecyclerViewAdapter


    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        initRecycler()
        makeInitialCalls()
        listeners()
    }

    private fun makeInitialCalls() {
        observe()
        firebaseAuthImpl.getUserId()?.let { viewModel.getUserInfo(it) }
        viewModel.getCommunityPosts()
    }

    private fun listeners() {
        binding.searchBar.doOnTextChanged { text, _, _, count ->
            if (text!!.length >= 2) {
                d("piifi", "$text $count")
                getSeachResultWithDelay(text.toString())
            } else if (text.isEmpty()) {
                viewModel.getCommunityPosts()
                observe()
            }
        }

        binding.userProfile.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_community_to_navigation_profile)
        }
    }

    private fun initRecycler() {

        binding.recentPostsRV.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        var userId = firebaseAuthImpl.getUserId()

        if (userId.isNullOrEmpty()) {
            userId = ""
        }

        adapter =
            CommunityPostsRecyclerViewAdapter(userId) { _, content, b ->
                if (b) {
                    viewModel.createLike(content.id, userId,content.user.id)
                } else {
                    viewModel.dislikePost(content.id, userId)
                }
            }
        binding.recentPostsRV.adapter = adapter.withLoadStateFooter(LoaderStateAdapter())

        adapter.onProfileClick = { uid ->
            val bundle = bundleOf("uid" to uid)
            binding.root.findNavController().navigate(
                R.id.action_navigation_community_to_navigation_profile,
                bundle
            )
        }

        adapter.onCardViewClick = { postId ->
            val bundle = bundleOf("postId" to postId)
            binding.root.findNavController().navigate(
                R.id.action_navigation_community_to_singleCommunityPostFragment,
                bundle
            )
        }

        binding.swipe.setOnRefreshListener {
            viewModel.getCommunityPosts()
            observe()
        }
    }

    private fun observe() {
        viewModel.readUserInfo.observe(viewLifecycleOwner, { data ->
            when (data.status) {
                Resource.Status.SUCCESS -> {
                    data.data?.let { binding.userProfile.loadProfileImg(it.url) }
                }
                Resource.Status.ERROR -> i("Like", "$data")
                Resource.Status.LOADING -> i("Like", "loading")
            }
        })

        viewModel.getCommunityPosts().observe(viewLifecycleOwner, { resource ->
            lifecycleScope.launch {
                if (binding.progressCircular.isVisible) {
                    binding.progressCircular.setGone()
                }
                binding.swipe.isRefreshing = false
                adapter.submitData(resource)
            }
        })

        viewModel.createLikeResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> i("Like", "$it")
                Resource.Status.SUCCESS -> i("Like", "sucess")
                Resource.Status.LOADING -> i("Like", "loading")
            }
        })

        viewModel.dislikeResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> i("dislike", "$it")
                Resource.Status.SUCCESS -> i("dislike", "sucess")
                Resource.Status.LOADING -> i("dislike", "loading")
            }
        })


    }

    private fun getSeachResultWithDelay(text: String) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(500)
            viewModel.searchPost(text).observe(viewLifecycleOwner, {
                d("tagtag123", "$it")
                lifecycleScope.launch {
                    if (binding.progressCircular.isVisible) {
                        binding.progressCircular.setGone()
                    }
                    adapter.submitData(it)
                    adapter.notifyDataSetChanged()
                }
            })
        }
    }

}