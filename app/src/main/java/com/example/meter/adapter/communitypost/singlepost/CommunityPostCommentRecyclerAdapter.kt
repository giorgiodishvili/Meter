package com.example.meter.adapter.communitypost.singlepost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.adapter.communitypost.main.onProfileClick
import com.example.meter.databinding.CommentItemLayoutBinding
import com.example.meter.entity.Comment
import com.example.meter.extensions.loadProfileImg

class CommunityPostCommentRecyclerAdapter(
    private val commentList: MutableList<Comment>
) : RecyclerView.Adapter<CommunityPostCommentRecyclerAdapter.ItemHolder>() {

    lateinit var onProfileClick: onProfileClick

    inner class ItemHolder(private val binding: CommentItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var comment: Comment
        fun onBind() {

            comment = commentList[absoluteAdapterPosition]

            binding.authorIV.loadProfileImg(comment.user.url)
            binding.name.text = comment.user.name
            binding.descriptionTV.text = comment.description
        }

        fun setListeners(){
            binding.authorIV.setOnClickListener{
                onProfileClick.invoke(comment.user.id)
            }
            binding.name.setOnClickListener {
                onProfileClick.invoke(comment.user.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            CommentItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind()
        holder.setListeners()
    }

    override fun getItemCount(): Int = commentList.size

    fun addComment(comment: Comment) {
        commentList.add(comment)
        notifyItemInserted(commentList.size - 1)
    }
}