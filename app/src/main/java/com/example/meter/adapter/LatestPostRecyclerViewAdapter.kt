package com.example.meter.adapter

import android.util.Log.i
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meter.R
import com.example.meter.databinding.RecentPostItemLayoutBinding
import com.example.meter.entity.PostItem

class LatestPostRecyclerViewAdapter(private val latestPosts: List<PostItem>,) :
    RecyclerView.Adapter<LatestPostRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        RecentPostItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = latestPosts.size

    inner class ViewHolder(private val binding: RecentPostItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            i("HERE", latestPosts[0].toString())
            var url = ""
            if(latestPosts[adapterPosition].photoUrl.isNotEmpty()){
                url = latestPosts[adapterPosition].photoUrl[0]
            }
            Glide
                .with(binding.root.context)
                .load(url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(binding.cardViewImage);
            binding.articleHeaderTV.text = latestPosts[adapterPosition].articleHeader
            binding.priceTV.text = latestPosts[adapterPosition].price.toString()
        }
    }
}