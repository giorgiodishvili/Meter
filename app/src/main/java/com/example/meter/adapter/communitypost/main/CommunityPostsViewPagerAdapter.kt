package com.example.meter.adapter.communitypost.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.R
import com.example.meter.databinding.CommunityPostPhotoItemBinding
import com.example.meter.extensions.loadImg
import com.example.meter.extensions.setGone


class CommunityPostsViewPagerAdapter(
    private val images: List<String>,
) : RecyclerView.Adapter<CommunityPostsViewPagerAdapter.ItemHolder>() {


    lateinit var onCardViewClick: onCardViewClick

    inner class ItemHolder(private val binding: CommunityPostPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
            if (images.size == 1) {
                binding.positionHolder.setGone()
                binding.imagePosition.setGone()
            } else {
                binding.imagePosition.text = binding.root.context.getString(
                    R.string.position_image,
                    absoluteAdapterPosition + 1,
                    images.size
                )
            }

            binding.imgSlider.loadImg(images[absoluteAdapterPosition])
            binding.imgSlider.clipToOutline = true
            binding.cardView.setOnClickListener {
                onCardViewClick.invoke(absoluteAdapterPosition.toLong())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            CommunityPostPhotoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int {
        return images.size
    }
}
