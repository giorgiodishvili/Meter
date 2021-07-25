package com.example.meter.screens.bottom_nav.community.singlepost

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.R
import com.example.meter.adapter.communitypost.singlepost.CommunityPostCommentRecyclerAdapter
import com.example.meter.adapter.communitypost.singlepost.SingleCommunityPostPhotoRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.SingleCommunityPostFragmentBinding
import com.example.meter.entity.Comment
import com.example.meter.entity.community.post.Content
import com.example.meter.extensions.loadImg
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SingleCommunityPostFragment :
    BaseFragment<SingleCommunityPostFragmentBinding, SingleCommunityPostViewModel>(
        SingleCommunityPostFragmentBinding::inflate,
        SingleCommunityPostViewModel::class.java
    ) {
    private lateinit var communityPostCommentRecyclerAdapter: CommunityPostCommentRecyclerAdapter

    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl

    private var userId: String = ""

    private var postId: Long = -1
    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        getDataFromBundle()
        makeInitialCalls()
        setListeners()
        observe()
    }

    private fun observe() {
        viewModel.post.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> Log.i("debugee", "ERROR")
                Resource.Status.SUCCESS -> {
                    it!!.data?.let { it1 -> setUpPost(it1) }
                }
                Resource.Status.LOADING -> Log.i("debugee", "loading")
            }
        })

        viewModel.comments.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> Log.i("debugee", "$it")
                Resource.Status.SUCCESS -> {
                    Log.i("debugee", it.data.toString())
                    if (it.data!!.isNotEmpty()) {

                        communityPostCommentRecyclerAdapter =
                            CommunityPostCommentRecyclerAdapter(it.data as MutableList<Comment>)
                        binding.singlePostRecyclerPhoto.adapter = communityPostCommentRecyclerAdapter
                        binding.singlePostRecyclerPhoto.layoutManager =
                            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                    }
                }
                Resource.Status.LOADING -> Log.i("debugee", "loading")
            }
        })

        viewModel.createComment.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> Log.i("debugee", "$it")
                Resource.Status.SUCCESS -> {
                    Log.i("debugee", it.data.toString())
                    communityPostCommentRecyclerAdapter.addComment(it.data!!)
                }
                Resource.Status.LOADING -> Log.i("debugee", "loading")
            }
        })

        viewModel.deleteComment.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> Log.i("debugee", "$it")
                Resource.Status.SUCCESS -> Log.i("debugee", it.data.toString())
                Resource.Status.LOADING -> Log.i("debugee", "loading")
            }
        })
    }

    private fun getDataFromBundle() {
        postId = arguments?.getLong("postId", -1L)!!
    }

    private fun makeInitialCalls() {
        viewModel.getPost(postId)
        viewModel.getComments(postId)
        firebaseAuthImpl.getUserId()?.let {

        }
    }

    private fun setListeners() {
        binding.commentBTN.setOnClickListener {

            val commentText = binding.commentET.text
            if (commentText.isNotEmpty()) {
                if (firebaseAuthImpl.getUserId() != null) {
                    userId = firebaseAuthImpl.getUserId()!!
                    viewModel.createComment(
                        postId,
                        userId, commentText.toString()
                    )
                    commentText.clear()
                } else {
                    binding.root.findNavController().navigate(R.id.action_global_navigation_profile)
                }
            }
        }
    }

    private fun setUpPost(data: Content) {
        binding.authorIV.loadImg(data.user.url)
        binding.name.text = data.user.name
        binding.descriptionTB.text = data.description
        binding.singlePostRecyclerPhoto.adapter = SingleCommunityPostPhotoRecyclerAdapter(data.photoCarUrl)
        binding.singlePostRecyclerPhoto.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }
}