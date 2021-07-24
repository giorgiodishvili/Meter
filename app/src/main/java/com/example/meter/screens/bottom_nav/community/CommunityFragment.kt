package com.example.meter.screens.bottom_nav.community

import android.util.Log.d
import android.util.Log.i
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meter.R
import com.example.meter.adapter.CommunityPostsRecyclerViewAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.CommunityFragmentBinding
import com.example.meter.extensions.setGone
import com.example.meter.network.Resource
import com.example.meter.paging.loadstate.LoaderStateAdapter
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
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

    companion object {
        private const val EXPAND_ID1 = 2131296463
        private const val EXPAND_ID2 = 2131296464
    }

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        initRecycler()
        transitionListener()
        makeInitialCalls()
    }

    private fun makeInitialCalls() {
        observe()
        viewModel.getCommunityPosts()
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
                    viewModel.createLike(content.id, userId)
                } else {
                    viewModel.dislikePost(content.id, userId)
                }
            }
        binding.recentPostsRV.adapter = adapter.withLoadStateFooter(LoaderStateAdapter())

        adapter.onProfileClick = { uid ->
            val bundle = bundleOf("uid" to uid)
            findNavController().navigate(R.id.action_navigation_community_to_navigation_profile, bundle)
        }
    }

    private fun observe() {
        viewModel.getCommunityPosts().observe(viewLifecycleOwner, { resource ->
            lifecycleScope.launch {
                if(binding.progressCircular.isVisible){
                    binding.progressCircular.setGone()
                }
                adapter.submitData(resource)
            }
        })

        viewModel.createLikeResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> i("debugee", "ERROR")
                Resource.Status.SUCCESS -> i("debugee", "sucess")
                Resource.Status.LOADING -> i("debugee", "loading")
            }
        })

        viewModel.dislikeResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> i("debugee", "ERROR")
                Resource.Status.SUCCESS -> i("debugee", "sucess")
                Resource.Status.LOADING -> i("debugee", "loading")
            }
        })
    }

    private fun transitionListener() {
        val transitionListener = object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, startId: Int, endId: Int) {
                //nothing to do
            }

            override fun onTransitionChange(p0: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                //nothing to do
            }

            override fun onTransitionCompleted(p0: MotionLayout?, currentId: Int) {
                if (currentId == EXPAND_ID1 || currentId == EXPAND_ID2) {
                    findNavController().navigate(R.id.action_navigation_community_to_navigation_profile)
                }
                d("trackemotion", "$currentId")
            }
            override fun onTransitionTrigger(p0: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {
                //not used here
            }

        }

        binding.motionLayout.setTransitionListener(transitionListener)
    }

}