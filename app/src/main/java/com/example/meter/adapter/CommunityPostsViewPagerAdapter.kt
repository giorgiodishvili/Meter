package com.example.meter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.databinding.CommunityPostPhotoItemBinding
import com.example.meter.extensions.loadImg


class CommunityPostsViewPagerAdapter(
    private val images: List<String>,
) : RecyclerView.Adapter<CommunityPostsViewPagerAdapter.ItemHolder>() {

    inner class ItemHolder(private val binding: CommunityPostPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {

            binding.imgSlider.loadImg(images[absoluteAdapterPosition])
            binding.imgSlider.clipToOutline = true

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
