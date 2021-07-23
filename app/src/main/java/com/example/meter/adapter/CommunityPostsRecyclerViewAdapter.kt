package com.example.meter.adapter

import android.util.Log.i
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.meter.databinding.CommunityWallPostItemBinding
import com.example.meter.entity.community.post.Content
import com.example.meter.extensions.hide
import com.example.meter.extensions.show

typealias onProfilePicClick = (uid: String) -> Unit
class CommunityPostsRecyclerViewAdapter :
    PagingDataAdapter<Content, CommunityPostsRecyclerViewAdapter.ItemHolder>(REPO_COMPARATOR) {

    lateinit var onProfilePicClick: onProfilePicClick


    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Content>() {
            override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean =
                oldItem.id == newItem.id
        }
    }


    inner class ItemHolder(private val binding: CommunityWallPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: Content
        fun bind() {
            item = getItem(absoluteAdapterPosition)!!
            binding.firstName.text = item.user.name.split(" ")[0]
            binding.lastName.text = item.user.name.split(" ")[1]
            binding.title.text = item.title
            binding.description.text = item.description

            i("ITEM", "$item")
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

            binding.leftArrBTN.setOnClickListener {
                binding.photos.currentItem = binding.photos.currentItem - 1
            }

            binding.rightArrBTN.setOnClickListener {
                binding.photos.currentItem = binding.photos.currentItem + 1
            }

            binding.authorIV.setOnClickListener {
                onProfilePicClick.invoke(item.user.id)
            }

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
