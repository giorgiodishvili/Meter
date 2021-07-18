package com.example.meter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.databinding.CommunityWallPostItemBinding
import com.example.meter.entity.community.post.Content

class CommunityPostsRecyclerViewAdapter(private val data: List<Content>) :
    RecyclerView.Adapter<CommunityPostsRecyclerViewAdapter.ItemHolder>() {


    inner class ItemHolder(private val binding: CommunityWallPostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder( CommunityWallPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return  data.size
    }

}
