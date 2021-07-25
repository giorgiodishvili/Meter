package com.example.meter.adapter

import android.util.Log.i
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meter.R
import com.example.meter.databinding.RecentPostItemLayoutBinding
import com.example.meter.entity.SellCarPost

class LatestPostRecyclerViewAdapter(private val latestSellCarPosts: List<SellCarPost>) :
    RecyclerView.Adapter<LatestPostRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        RecentPostItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = latestSellCarPosts.size

    inner class ViewHolder(private val binding: RecentPostItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            i("HERE", latestSellCarPosts[0].toString())
            var url = ""
            if (latestSellCarPosts[absoluteAdapterPosition].photoUrl.isNotEmpty()) {
                url = latestSellCarPosts[absoluteAdapterPosition].photoUrl[0]
            }
            Glide
                .with(binding.root.context)
                .load(url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(binding.cardViewImage);
            binding.articleHeaderTV.text = latestSellCarPosts[adapterPosition].articleHeader
            binding.priceTV.text = latestSellCarPosts[adapterPosition].price.toString()
        }
    }
}