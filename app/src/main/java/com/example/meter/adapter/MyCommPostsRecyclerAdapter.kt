package com.example.meter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.databinding.CommunityWallPostItemBinding
import com.example.meter.entity.UserDetails
import com.example.meter.entity.community.post.MyPost
import com.example.meter.extensions.setGone

class MyCommPostsRecyclerAdapter: RecyclerView.Adapter<MyCommPostsRecyclerAdapter.ViewHolder>() {
    private val posts: MutableList<MyPost> = mutableListOf()
    private lateinit var userInfo: UserDetails

    inner class ViewHolder(private val binding: CommunityWallPostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        lateinit var model: MyPost

        fun bind() {
            binding.authorIV.setGone()
            binding.lastName.setGone()
            binding.firstName.setGone()

            model = posts[absoluteAdapterPosition]
            binding.postLikeTV.text = model.likeAmount.toString()
            binding.postCommentTV.text = model.commentsAmount.toString()
            binding.photos.adapter = CommunityPostsViewPagerAdapter(posts.photoCarUrl)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CommunityWallPostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = posts.size

    fun fetchPosts(posts: MutableList<MyPost>) {
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