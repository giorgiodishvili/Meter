package com.example.meter.adapter.communitypost.main

import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.meter.R
import com.example.meter.databinding.CommunityWallPostItemBinding
import com.example.meter.databinding.CommunityWallWithoutPhotoPostItemBinding
import com.example.meter.entity.community.post.Content
import com.example.meter.extensions.hide
import com.example.meter.extensions.loadImg
import com.example.meter.extensions.show
import com.example.meter.utils.transformers.DepthTransformer


typealias onProfileClick = (uid: String) -> Unit
typealias onCardViewClick = (postId: Long) -> Unit

class CommunityPostsRecyclerViewAdapter(
    private val userId: String, private val likeButtonOnClick: (View, Content, Boolean) -> Unit,
) :
    PagingDataAdapter<Content, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    private lateinit var communityPostsViewPagerAdapter: CommunityPostsViewPagerAdapter

    companion object {
        private const val DEFAULT_ITEM = 0
        private const val NO_PHOTO_ITEM = 1

        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Content>() {
            override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean =
                oldItem.id == newItem.id
        }
    }


    lateinit var onProfileClick: onProfileClick
    lateinit var onCardViewClick: onCardViewClick

    inner class NoPhotoHolder(private val binding: CommunityWallWithoutPhotoPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: Content
        fun bind() {
            setDataToView()
            setListeners()
        }

        private fun setDataToView() {
            item = getItem(absoluteAdapterPosition)!!
            i("ITEM", "$item")
            binding.firstName.text = item.user.name.split(" ")[0]
            if (item.user.name.split(" ").size != 2) {
                binding.firstName.text = item.user.name.split(" ")[0]
                binding.lastName.text = item.user.name.split(" ")[1]
            }
            binding.title.text = item.title
            binding.authorIV.loadImg(item.user.url)
            binding.descriptionCenter.text = item.description
            binding.postCommentTV.text = item.commentsAmount.toString()
            binding.postLikeTV.text = item.likedUserIds.size.toString()
            binding.postViewTV.text = item.views.toString()

            if (item.likedUserIds.contains(userId)) {
                binding.postLikeBTN.setImageResource(R.drawable.ic_like)
            } else {
                binding.postLikeBTN.setImageResource(R.drawable.ic_like_unpressed)
            }
        }

        private fun setListeners() {

            binding.authorIV.setOnClickListener {
                onProfileClick.invoke(item.user.id)
            }

            binding.photos.setOnClickListener {
                onCardViewClick.invoke(item.id)
            }


            binding.postCommentBTN.setOnClickListener {
                onCardViewClick.invoke(item.id)
            }

            binding.descriptionCenter.setOnClickListener {
                onCardViewClick.invoke(item.id)
            }

            binding.postLikeBTN.setOnClickListener {
                when {
                    item.likedUserIds.contains(userId) -> {
                        //                    binding.postLikeTV.text =
                        //                        (binding.postLikeTV.text.toString().toLong() - 1).toString()
                        likeButtonOnClick.invoke(it, item, false)
                        item.likedUserIds.remove(userId)
                        binding.postLikeTV.text = (item.likedUserIds.size).toString()
                        binding.postLikeBTN.setImageResource(R.drawable.ic_like_unpressed)

                    }
                    userId.isNotEmpty() -> {
                        //                    binding.postLikeTV.text =
                        //                        (binding.postLikeTV.text.toString().toLong() + 1).toString()
                        likeButtonOnClick.invoke(it, item, true)
                        item.likedUserIds.add(userId)
                        binding.postLikeTV.text = (item.likedUserIds.size).toString()
                        binding.postLikeBTN.setImageResource(R.drawable.ic_like)
                    }
                    else -> {
                        it.findNavController().navigate(R.id.action_global_navigation_profile)
                    }
                }
            }
        }
    }

    inner class ItemHolder(private val binding: CommunityWallPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: Content
        fun bind() {
            setDataToView()
            manipulateArrows()
            setListeners()
        }

        private fun setDataToView() {
            item = getItem(absoluteAdapterPosition)!!
            i("ITEM", "$item")
            if (item.user.name.split(" ").size != 1) {
                binding.firstName.text = item.user.name.split(" ")[0]
                binding.lastName.text = item.user.name.split(" ")[1]
            } else {
                binding.firstName.text = item.user.name
            }
            binding.title.text = item.title
            binding.authorIV.loadImg(item.user.url)
            binding.description.text = item.description
            binding.postCommentTV.text = item.commentsAmount.toString()
            binding.postLikeTV.text = item.likedUserIds.size.toString()
            binding.postViewTV.text = item.views.toString()

            if (item.likedUserIds.contains(userId)) {
                binding.postLikeBTN.setImageResource(R.drawable.ic_like)
            } else {
                binding.postLikeBTN.setImageResource(R.drawable.ic_like_unpressed)
            }


        }


        private fun setListeners() {

            binding.authorIV.setOnClickListener {
                onProfileClick.invoke(item.user.id)
            }


            communityPostsViewPagerAdapter.onCardViewClick = {
                onCardViewClick.invoke(item.id)
            }



            binding.leftArrBTN.setOnClickListener {
                binding.photos.currentItem = binding.photos.currentItem - 1
            }

            binding.rightArrBTN.setOnClickListener {
                binding.photos.currentItem = binding.photos.currentItem + 1
            }

            binding.postCommentBTN.setOnClickListener {
                onCardViewClick.invoke(item.id)

            }

            binding.description.setOnClickListener {
                onCardViewClick.invoke(item.id)
            }
            binding.postLikeBTN.setOnClickListener {
                when {
                    item.likedUserIds.contains(userId) -> {
                        //                    binding.postLikeTV.text =
                        //                        (binding.postLikeTV.text.toString().toLong() - 1).toString()
                        likeButtonOnClick.invoke(it, item, false)
                        item.likedUserIds.remove(userId)
                        binding.postLikeTV.text = (item.likedUserIds.size).toString()
                        binding.postLikeBTN.setImageResource(R.drawable.ic_like_unpressed)

                    }
                    userId.isNotEmpty() -> {
                        //                    binding.postLikeTV.text =
                        //                        (binding.postLikeTV.text.toString().toLong() + 1).toString()
                        likeButtonOnClick.invoke(it, item, true)
                        item.likedUserIds.add(userId)
                        binding.postLikeTV.text = (item.likedUserIds.size).toString()
                        binding.postLikeBTN.setImageResource(R.drawable.ic_like)
                    }
                    else -> {
                        it.findNavController().navigate(R.id.action_global_navigation_profile)
                    }
                }
            }

        }

        private fun manipulateArrows() {
            if (item.photoCarUrl.isEmpty()) {
                binding.rightArrBTN.hide()
                binding.leftArrBTN.hide()
                if (binding.photos.isVisible) {
                    binding.photos.hide()
                }
            } else {
                if (!binding.photos.isVisible) {
                    binding.photos.show()
                }
                communityPostsViewPagerAdapter =
                    CommunityPostsViewPagerAdapter(item.photoCarUrl)
                binding.photos.adapter = communityPostsViewPagerAdapter
                binding.photos.setPageTransformer(DepthTransformer)

            }


            binding.photos.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    when {
                        1 == item.photoCarUrl.size -> {
                            binding.rightArrBTN.hide()
                            binding.leftArrBTN.hide()
                        }
                        position == item.photoCarUrl.size - 1 -> {
                            binding.rightArrBTN.hide()
                            binding.leftArrBTN.show()
                        }
                        position == 0 -> {
                            binding.rightArrBTN.show()
                            binding.leftArrBTN.hide()
                        }
                        else -> {
                            binding.rightArrBTN.show()
                            binding.leftArrBTN.show()
                        }
                    }
                }
            })
        }


    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)!!
        return if (item.photoCarUrl.isEmpty()) {
            NO_PHOTO_ITEM
        } else {
            DEFAULT_ITEM
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemHolder -> holder.bind()
            is NoPhotoHolder -> holder.bind()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DEFAULT_ITEM) {
            ItemHolder(
                CommunityWallPostItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            NoPhotoHolder(
                CommunityWallWithoutPhotoPostItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
