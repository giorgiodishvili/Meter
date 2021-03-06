package com.example.meter.screens.bottom_nav.profile.myposts.commPosts

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meter.R
import com.example.meter.adapter.mypost.MyCommPostsRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.base.SharedViewModel
import com.example.meter.databinding.MyCommPostsFragmentBinding
import com.example.meter.extensions.hide
import com.example.meter.extensions.show
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyCommPostsFragment : BaseFragment<MyCommPostsFragmentBinding, MyCommPostsViewModel>(
    MyCommPostsFragmentBinding::inflate,
    MyCommPostsViewModel::class.java
) {
    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: MyCommPostsRecyclerAdapter


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
                binding.progressCircular.show()
                sharedViewModel.saveUserId(currentUid)
                viewModel.getUserInfo(currentUid)
                observers(currentUid)
            }
        })

//        uid = arguments?.getString("uid2").toString() ?: firebaseAuthImpl.getUserId().toString()

    }

    private fun initRecycler(uid: String) {
        adapter =
            MyCommPostsRecyclerAdapter(uid) { _, content, b ->
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
        adapter.onCardViewClick = { postId ->
            val bundle = bundleOf("postId" to postId)
            val navController =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController.navController.navigate(
                R.id.action_global_singleCommunityPostFragment,
                bundle
            )
        }

    }

    private fun observers(uid: String) {

        viewModel.readUserPosts.observe(this, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
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
                    initRecycler(uid)
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