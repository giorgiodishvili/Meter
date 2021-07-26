package com.example.meter.adapter.communitypost.singlepost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.databinding.SinglePostPhotoItemLayoutBinding
import com.example.meter.extensions.loadImg

class SingleCommunityPostPhotoRecyclerAdapter(
    private val photoUrlList: List<String>
) : RecyclerView.Adapter<SingleCommunityPostPhotoRecyclerAdapter.ItemHolder>() {

    inner class ItemHolder(private val binding: SinglePostPhotoItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
            binding.photo.loadImg(photoUrlList[absoluteAdapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            SinglePostPhotoItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int = photoUrlList.size
}