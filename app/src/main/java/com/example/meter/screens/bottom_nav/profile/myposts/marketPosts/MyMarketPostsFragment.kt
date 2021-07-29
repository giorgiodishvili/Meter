package com.example.meter.screens.bottom_nav.profile.myposts.marketPosts

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.meter.R
import com.example.meter.adapter.mypost.MyMarketPostsRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.base.SharedViewModel
import com.example.meter.databinding.MyMarketPostsFragmentBinding
import com.example.meter.extensions.hide
import com.example.meter.extensions.show
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
//            binding.progressCircular.show()

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
    }

    private fun initRecycler() {
        adapter =
            MyMarketPostsRecyclerAdapter()
        binding.recycler.layoutManager = GridLayoutManager(
            requireContext(),
            3
        )
        binding.recycler.adapter = adapter
        adapter.onCardViewClick = { postId ->
            val bundle = bundleOf("postId" to postId)
            val navController =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController.navController.navigate(
                R.id.action_global_singleCarSellPostFragment,
                bundle
            )
        }
    }

    private fun observers(uid: String) {

        viewModel.readUserPosts.observe(this, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressCircular.hide()
                    it.data?.let { posts -> adapter.fetchPosts(posts.toMutableList()) }
                }
                Resource.Status.ERROR -> {
                    binding.progressCircular.hide()
                }
                Resource.Status.LOADING -> {
                    binding.progressCircular.show()
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