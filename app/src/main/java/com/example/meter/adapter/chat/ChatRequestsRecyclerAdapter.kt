package com.example.meter.adapter.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.databinding.UserchatItemBinding
import com.example.meter.entity.UserDetails
import com.example.meter.extensions.loadProfileImg

typealias onUserClick = (userPosition: Int) -> Unit
class ChatRequestsRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val users: MutableList<UserDetails> = mutableListOf()
    lateinit var onUserClick: onUserClick

    inner class ViewHolder(private val binding: UserchatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var model: UserDetails
        fun bind() {
            model = users[absoluteAdapterPosition]
            binding.userImg.loadProfileImg(model.url)
            binding.userName.text = model.name
            binding.userImg.setOnClickListener {
                onUserClick.invoke(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            UserchatItemBinding.inflate(
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

    override fun getItemCount(): Int = users.size

    fun addItems(chatItem: List<UserDetails>) {
        this.users.clear()
        this.users.addAll(chatItem)
        notifyDataSetChanged()
    }

}