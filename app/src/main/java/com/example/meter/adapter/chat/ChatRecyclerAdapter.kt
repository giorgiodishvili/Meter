package com.example.meter.adapter.chat

import android.util.Log.d
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.R
import com.example.meter.databinding.ChatItemBinding
import com.example.meter.entity.Chat
import com.example.meter.extensions.loadProfileImg
import com.example.meter.extensions.mirrorView

class ChatRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val MY_MSG = 0
        const val OTHERS_MSG = 1
    }

    private val chatItems: MutableList<Chat> = mutableListOf()
    private var myImg: String = ""
    private var otherImg: String = ""
    private var myUid: String = ""

    inner class LeftViewHolder(private val binding: ChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var model: Chat
        fun bind() {
            model = chatItems[absoluteAdapterPosition]
            binding.msgText.text = model.text
            if (itemViewType == MY_MSG) {

                binding.userImg.loadProfileImg(myImg)
                binding.msgText.setBackgroundResource(R.drawable.button_shape)
                binding.root.mirrorView()
            } else {
                binding.userImg.loadProfileImg(otherImg)
                binding.msgText.setBackgroundResource(R.drawable.custom_button)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LeftViewHolder(
            ChatItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LeftViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int = chatItems.size

    fun addItems(chatItem: Chat) {
        this.chatItems.add(chatItem)
        notifyItemInserted(chatItems.size - 1)

    }

    fun preloadChatItems(chatItems: MutableList<Chat>) {
        this.chatItems.clear()
        this.chatItems.addAll(chatItems)
        notifyDataSetChanged()
    }

    fun loadInfo(myUid: String, myImg: String, otherImg: String) {
        d("userIdLog123", "$myUid")
        this.myUid = myUid
        this.myImg = myImg
        this.otherImg = otherImg
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatItems[position].fromUid == myUid)
            MY_MSG
        else
            OTHERS_MSG
    }
}