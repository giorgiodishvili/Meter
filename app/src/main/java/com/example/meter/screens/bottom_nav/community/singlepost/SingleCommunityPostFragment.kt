package com.example.meter.screens.bottom_nav.community.singlepost

import android.util.Log.d
import android.util.Log.i
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.R
import com.example.meter.adapter.communitypost.singlepost.CommunityPostCommentRecyclerAdapter
import com.example.meter.adapter.communitypost.singlepost.SingleCommunityPostPhotoRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.SingleCommunityPostFragmentBinding
import com.example.meter.entity.Comment
import com.example.meter.entity.community.post.Content
import com.example.meter.extensions.loadProfileImg
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

    private lateinit var content: Content

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {

        getDataFromBundle()
        makeInitialCalls()
        setListeners()
        observe()
        setUpViewElements()
    }

    private fun observe() {
        viewModel.readUserInfo.observe(viewLifecycleOwner, {
            when (it.status) {

                Resource.Status.SUCCESS -> {
                    d("tagtag", it.data!!.url)
                    it.data.let { it1 -> binding.selfProfilePhoto.loadProfileImg(it1.url) }
                }
                Resource.Status.ERROR -> i("debugee", "ERROR")
                Resource.Status.LOADING -> i("debugee", "loading")
            }
        })

        viewModel.post.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> i("debugee", "ERROR")
                Resource.Status.SUCCESS -> {
                    it!!.data?.let { it1 ->
                        setUpPost(it1)
                        content = it.data!!
                    }
                }
                Resource.Status.LOADING -> i("debugee", "loading")
            }
        })

        viewModel.comments.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> i("debugee", "$it")
                Resource.Status.SUCCESS -> {
                    i("debugee", it.data.toString())
                        initCommentRecycler(it)
                    
                }
                Resource.Status.LOADING -> i("debugee", "loading")
            }
        })

        viewModel.createComment.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> i("debugee", "$it")
                Resource.Status.SUCCESS -> {
                    i("debugee", it.data.toString())
                    communityPostCommentRecyclerAdapter.addComment(it.data!!)
                }
                Resource.Status.LOADING -> i("debugee", "loading")
            }
        })

        viewModel.deleteComment.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> i("debugee", "$it")
                Resource.Status.SUCCESS -> i("debugee", it.data.toString())
                Resource.Status.LOADING -> i("debugee", "loading")
            }
        })

        viewModel.createLike.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> i("like", "$it")
                Resource.Status.SUCCESS -> i("like", it.data.toString())
                Resource.Status.LOADING -> i("like", "loading")
            }
        })

        viewModel.dislike.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> i("dislike", "$it")
                Resource.Status.SUCCESS -> i("dislike", it.data.toString())
                Resource.Status.LOADING -> i("dislike", "loading")
            }
        })

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_singleCommunityPostFragment_to_navigation_community)
        }
    }

    private fun initCommentRecycler(it: Resource<List<Comment>>) {
        communityPostCommentRecyclerAdapter =
            CommunityPostCommentRecyclerAdapter(it.data as MutableList<Comment>)
        binding.commentRV.adapter = communityPostCommentRecyclerAdapter
        binding.commentRV.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        communityPostCommentRecyclerAdapter.onProfileClick = {
            binding.root.findNavController().navigate(
                R.id.action_singleCommunityPostFragment_to_navigation_profile,
                bundleOf("uid" to it)
            )
        }
    }

    private fun getDataFromBundle() {
        postId = arguments?.getLong("postId", -1L)!!
    }

    private fun makeInitialCalls() {
        firebaseAuthImpl.getUserId()?.let {
            viewModel.getUserInfo(it)
            userId = firebaseAuthImpl.getUserId()!!
        }

        viewModel.getPost(postId)
        viewModel.getComments(postId)
    }

    private fun setListeners() {
        binding.commentBTN.setOnClickListener {

            val commentText = binding.commentET.text
            if (commentText.isNotEmpty()) {
                if (userId.isNotEmpty()) {
                    viewModel.createComment(
                        postId,
                        userId, commentText.toString()
                    )
                    commentText.clear()
                } else {
                    binding.root.findNavController()
                        .navigate(R.id.action_singleCommunityPostFragment_to_navigation_profile)
                }
            }
        }

        binding.commentBTNLogo.setOnClickListener {
            binding.commentET.requestFocus()
            binding.commentET.isFocusableInTouchMode = true
            binding.commentET.showSoftInputOnFocus = true

        }

        binding.likeButton.setOnClickListener {
            i("likeButton", userId)
            i("likeButton", this::content.isInitialized.toString())
            if (this::content.isInitialized && userId.isNotEmpty()) {
                if (content.likedUserIds.contains(userId)) {
                    viewModel.deleteLike(userId, postId)
                    binding.likeButton.setImageResource(R.drawable.ic_like_unpressed)
                    content.likedUserIds.remove(userId)
                } else {
                    viewModel.createLike(userId, postId)
                    content.likedUserIds.add(userId)
                    binding.likeButton.setImageResource(R.drawable.ic_like)
                }
                binding.likesAmount.text = (content.likedUserIds.size).toString()
            } else {
                binding.root.findNavController()
                    .navigate(R.id.action_singleCommunityPostFragment_to_navigation_profile)
            }
        }

        binding.selfProfilePhoto.setOnClickListener {
            binding.root.findNavController()
                .navigate(R.id.action_singleCommunityPostFragment_to_navigation_profile, bundleOf("uid" to userId))
        }
    }

    private fun setUpPost(data: Content) {

        binding.authorIV.loadProfileImg(data.user.url)
        binding.likesAmount.text = data.likeAmount.toString()
        binding.commentsAmount.text = data.commentsAmount.toString()
        if (data.likedUserIds.contains(userId)) {
            binding.likeButton.setImageResource(R.drawable.ic_like)
        } else {
            binding.likeButton.setImageResource(R.drawable.ic_like_unpressed)

        }
        binding.name.text = data.user.name
        binding.descriptionTB.text = data.description
        binding.singlePostRecyclerPhoto.adapter =
            SingleCommunityPostPhotoRecyclerAdapter(data.photoCarUrl)
        binding.singlePostRecyclerPhoto.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    private fun setUpViewElements() {
//        binding.scrollView.post {
//            binding.scrollView.scrollTo(
//                binding.likeButton.scrollX,
//                binding.likeButton.scrollY
//            )
//        }
    }
}