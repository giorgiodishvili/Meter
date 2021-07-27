package com.example.meter.adapter.mypost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.adapter.communitypost.main.onCardViewClick
import com.example.meter.databinding.MymarketPhotoItemBinding
import com.example.meter.entity.UserDetails
import com.example.meter.entity.sell.SellCarPostForMainPage
import com.example.meter.extensions.loadImg

class MyMarketPostsRecyclerAdapter(
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val posts: MutableList<SellCarPostForMainPage> = mutableListOf()
    private lateinit var userInfo: UserDetails

    lateinit var onCardViewClick: onCardViewClick

    inner class ViewHolder(private val binding: MymarketPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: SellCarPostForMainPage
        fun bind() {
            item = posts[absoluteAdapterPosition]
            binding.image.loadImg(item.photoUrl[0])
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            MymarketPhotoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int = posts.size

    fun fetchPosts(posts: MutableList<SellCarPostForMainPage>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    fun getUserInfo(userInfo: UserDetails?) {
        if (userInfo != null) {
            this.userInfo = userInfo
            notifyDataSetChanged()
        }
    }

}