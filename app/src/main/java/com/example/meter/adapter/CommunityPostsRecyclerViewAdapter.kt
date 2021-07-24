package com.example.meter.adapter

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
import com.example.meter.entity.community.post.Content
import com.example.meter.extensions.hide
import com.example.meter.extensions.show
import com.example.meter.utils.transformers.DepthTransformer


typealias onProfileClick = (uid: String) -> Unit
class CommunityPostsRecyclerViewAdapter(
    private val userId: String, private val likeButtonOnClick: (View,Content,Boolean) -> Unit
) :
    PagingDataAdapter<Content, CommunityPostsRecyclerViewAdapter.ItemHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Content>() {
            override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean =
                oldItem.id == newItem.id
        }
    }

    lateinit var onProfileClick: onProfileClick

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
            binding.firstName.text = item.user.name.split(" ")[0]
            binding.lastName.text = item.user.name.split(" ")[1]
            binding.title.text = item.title
            binding.description.text = item.description
            binding.postCommentTV.text = item.commentsAmount.toString()
            binding.postLikeTV.text = item.likedUserIds.size.toString()
            binding.postViewTV.text = item.views.toString()

            if (item.likedUserIds.contains(userId)) {
                binding.postLikeBTN.hide()
            } else {
                binding.postLikeBTN.show()
            }
        }


        private fun setListeners() {

            binding.authorIV.setOnClickListener {
                onProfileClick.invoke(item.user.id)
            }

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
                binding.photos.adapter = CommunityPostsViewPagerAdapter(item.photoCarUrl)
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

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            CommunityWallPostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}
