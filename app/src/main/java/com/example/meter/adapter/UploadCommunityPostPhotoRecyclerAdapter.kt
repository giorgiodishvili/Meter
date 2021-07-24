package com.example.meter.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.databinding.UploadCommunityPostPhotoRecyclerItemBinding
import com.example.meter.extensions.loadImgUri

class UploadCommunityPostPhotoRecyclerAdapter :
    RecyclerView.Adapter<UploadCommunityPostPhotoRecyclerAdapter.ItemHolder>() {

    private var uris: List<Uri> = mutableListOf()

    inner class ItemHolder(private val binding: UploadCommunityPostPhotoRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
            binding.imgView.loadImgUri(uris[absoluteAdapterPosition])
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemHolder {

        return ItemHolder(
            UploadCommunityPostPhotoRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ItemHolder,
        position: Int
    ) {
        holder.onBind()
    }

    override fun getItemCount(): Int = uris.size

    fun submitData(uris: List<Uri>) {
        this.uris = uris
        notifyDataSetChanged()
    }
}