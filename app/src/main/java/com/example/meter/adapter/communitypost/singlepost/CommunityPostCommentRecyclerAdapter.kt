package com.example.meter.adapter.communitypost.singlepost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.databinding.CommentItemLayoutBinding
import com.example.meter.entity.Comment
import com.example.meter.extensions.loadImg

class CommunityPostCommentRecyclerAdapter(
    private val commentList: MutableList<Comment>
) : RecyclerView.Adapter<CommunityPostCommentRecyclerAdapter.ItemHolder>() {

    inner class ItemHolder(private val binding: CommentItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var comment: Comment
        fun onBind() {
            comment = commentList[absoluteAdapterPosition]
            binding.authorIV.loadImg(comment.user.url)
            binding.name.text = comment.user.name
            binding.descriptionTV.text = comment.description
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
    }

    override fun getItemCount(): Int = commentList.size

    fun addComment(comment: Comment){
        commentList.add(comment)
        notifyItemInserted(commentList.size-1)
    }
}