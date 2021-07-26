package com.example.meter.adapter

import android.util.Log.i
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meter.R
import com.example.meter.databinding.RecentPostItemLayoutBinding
import com.example.meter.entity.sell.SellCarPostForMainPage

class LatestPostRecyclerViewAdapter(private val latestSellCarPostForMainPages: List<SellCarPostForMainPage>) :
    RecyclerView.Adapter<LatestPostRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        RecentPostItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = latestSellCarPostForMainPages.size

    inner class ViewHolder(private val binding: RecentPostItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            i("HERE", latestSellCarPostForMainPages[0].toString())
            var url = ""
            if (latestSellCarPostForMainPages[absoluteAdapterPosition].photoUrl.isNotEmpty()) {
                url = latestSellCarPostForMainPages[absoluteAdapterPosition].photoUrl[0]
            }
            Glide
                .with(binding.root.context)
                .load(url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(binding.cardViewImage);
            binding.articleHeaderTV.text = latestSellCarPostForMainPages[adapterPosition].articleHeader
            binding.priceTV.text = latestSellCarPostForMainPages[adapterPosition].price.toString()
        }
    }
}