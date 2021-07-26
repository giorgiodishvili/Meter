package com.example.meter.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.meter.R
import com.example.meter.adapter.communitypost.main.CommunityPostsViewPagerAdapter
import com.example.meter.adapter.communitypost.main.onCardViewClick
import com.example.meter.databinding.CarSellPostItemBinding
import com.example.meter.databinding.CommunityWallPostItemBinding
import com.example.meter.databinding.CommunityWallWithoutPhotoPostItemBinding
import com.example.meter.entity.UserDetails
import com.example.meter.entity.community.post.Content
import com.example.meter.entity.sell.SellCarPostForMainPage
import com.example.meter.extensions.hide
import com.example.meter.extensions.setGone
import com.example.meter.extensions.show
import com.example.meter.utils.transformers.DepthTransformer

class MyCommPostsRecyclerAdapter(
    private val userId: String,
    private val likeButtonOnClick: (View, Content, Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val posts: MutableList<SellCarPostForMainPage> = mutableListOf()
    private lateinit var userInfo: UserDetails

    companion object {
        private const val DEFAULT_ITEM = 0
        private const val NO_PHOTO_ITEM = 1
    }

    lateinit var onCardViewClick: onCardViewClick
    private lateinit var communityPostsViewPagerAdapter: CommunityPostsViewPagerAdapter


    inner class ViewHolder(private val binding: CarSellPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: SellCarPostForMainPage
        fun bind() {
            setDataToView()
            manipulateArrows()
            setListeners()
        }

        private fun setDataToView() {

            binding.authorIV.setGone()
            binding.firstName.setGone()
            binding.lastName.setGone()

            item = posts[absoluteAdapterPosition]
            Log.i("ITEM233", "$item")
            binding.firstName.text = item.user.name.split(" ")[0]
            binding.lastName.text = item.user.name.split(" ")[1]
            binding.postLikeTV.text = item.likedUserIds.size.toString()
            binding.title.text = item.title
            binding.description.text = item.description
            binding.postCommentTV.text = item.commentsAmount.toString()
            binding.postViewTV.text = item.views.toString()
        }

        private fun setListeners() {
            binding.leftArrBTN.setOnClickListener {
                binding.photos.currentItem = binding.photos.currentItem - 1
            }

            binding.rightArrBTN.setOnClickListener {
                binding.photos.currentItem = binding.photos.currentItem + 1
            }


            binding.postLikeBTN.setOnClickListener {
                when {
                    item.likedUserIds.contains(userId) -> {
                        //                    binding.postLikeTV.text =
                        //                        (binding.postLikeTV.text.toString().toLong() - 1).toString()
                        likeButtonOnClick.invoke(it, item, false)
                        item.likedUserIds.remove(userId)
                        binding.postLikeTV.text = (item.likedUserIds.size).toString()
                    }
                    userId.isNotEmpty() -> {
                        //                    binding.postLikeTV.text =
                        //                        (binding.postLikeTV.text.toString().toLong() + 1).toString()
                        likeButtonOnClick.invoke(it, item, true)
                        item.likedUserIds.add(userId)
                        binding.postLikeTV.text = (item.likedUserIds.size).toString()
                    }
                }
            }


            communityPostsViewPagerAdapter.onCardViewClick = {
                onCardViewClick.invoke(item.id)
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
                communityPostsViewPagerAdapter = CommunityPostsViewPagerAdapter(item.photoCarUrl)
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

    inner class NoImageHolder(private val binding: CommunityWallWithoutPhotoPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: Content
        fun bind() {
            setDataToView()
            setListeners()
        }

        private fun setDataToView() {

            binding.authorIV.setGone()
            binding.firstName.setGone()
            binding.lastName.setGone()

            item = posts[absoluteAdapterPosition]
            Log.i("ITEM233", "$item")
            binding.firstName.text = item.user.name.split(" ")[0]
            binding.lastName.text = item.user.name.split(" ")[1]
            binding.title.text = item.title
            binding.descriptionCenter.text = item.description
            binding.postCommentTV.text = item.commentsAmount.toString()
            binding.postLikeTV.text = item.likedUserIds.size.toString()
            binding.postViewTV.text = item.views.toString()

        }

        private fun setListeners() {

            binding.postLikeBTN.setOnClickListener {
                when {
                    item.likedUserIds.contains(userId) -> {
                        //                    binding.postLikeTV.text =
                        //                        (binding.postLikeTV.text.toString().toLong() - 1).toString()
                        likeButtonOnClick.invoke(it, item, false)
                        item.likedUserIds.remove(userId)
                        binding.postLikeTV.text = (item.likedUserIds.size).toString()
                    }
                    userId.isNotEmpty() -> {
                        //                    binding.postLikeTV.text =
                        //                        (binding.postLikeTV.text.toString().toLong() + 1).toString()
                        likeButtonOnClick.invoke(it, item, true)
                        item.likedUserIds.add(userId)
                        binding.postLikeTV.text = (item.likedUserIds.size).toString()
                    }
                }
            }

            communityPostsViewPagerAdapter.onCardViewClick = {
                onCardViewClick.invoke(item.id)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DEFAULT_ITEM) {
            ViewHolder(
                CommunityWallPostItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            NoImageHolder(
                CommunityWallWithoutPhotoPostItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind()
            is NoImageHolder -> holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = posts[position]
        return if (item.photoCarUrl.isEmpty()) {
            NO_PHOTO_ITEM
        } else {
            DEFAULT_ITEM
        }
    }

    override fun getItemCount(): Int = posts.size

    fun fetchPosts(posts: MutableList<SellCarPostForMainPage>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    fun getUserInfo(userInfo: UserDetails?) {
        if (userInfo != null) {
            this.userInfo = userInfo
            notifyDataSetChanged()
        }
    }

}