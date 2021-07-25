package com.example.meter.adapter.communitypost.upload

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.databinding.UploadCommunityPostButtonItemBinding
import com.example.meter.databinding.UploadCommunityPostPhotoRecyclerItemBinding
import com.example.meter.extensions.loadImgUri

typealias uploadButton = (position: Int) -> Unit

class UploadCommunityPostPhotoRecyclerAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var uploadButton: uploadButton

    companion object {
        private const val DEFAULT_UPLOAD_BUTTON = 1
        private const val NORMAL_ITEM = 0
    }

    private var uris: List<Uri> = mutableListOf()

    inner class ItemHolder(private val binding: UploadCommunityPostPhotoRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
            binding.imgView.loadImgUri(uris[absoluteAdapterPosition], true)
        }
    }

    inner class ButtonHolder(private val binding: UploadCommunityPostButtonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
            binding.uploadButton.setOnClickListener {
                uploadButton.invoke(0)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == NORMAL_ITEM) {
            ItemHolder(
                UploadCommunityPostPhotoRecyclerItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ButtonHolder(
                UploadCommunityPostButtonItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UploadCommunityPostPhotoRecyclerAdapter.ItemHolder -> holder.onBind()
            is UploadCommunityPostPhotoRecyclerAdapter.ButtonHolder -> holder.onBind()
        }
    }


    override fun getItemCount(): Int = uris.size

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            DEFAULT_UPLOAD_BUTTON
        } else
            NORMAL_ITEM
    }

    fun submitData(uris: List<Uri>) {
        this.uris = uris
        notifyDataSetChanged()
    }
}