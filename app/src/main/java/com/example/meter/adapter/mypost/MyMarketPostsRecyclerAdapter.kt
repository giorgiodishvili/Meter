package com.example.meter.adapter.mypost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.meter.adapter.communitypost.main.CommunityPostsViewPagerAdapter
import com.example.meter.adapter.communitypost.main.onCardViewClick
import com.example.meter.databinding.CarSellPostItemBinding
import com.example.meter.entity.UserDetails
import com.example.meter.entity.sell.SellCarPostForMainPage
import com.example.meter.extensions.hide
import com.example.meter.extensions.show
import com.example.meter.extensions.toFormattedDate
import com.example.meter.utils.transformers.DepthTransformer

class MyMarketPostsRecyclerAdapter(
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val posts: MutableList<SellCarPostForMainPage> = mutableListOf()
    private lateinit var userInfo: UserDetails

    lateinit var onCardViewClick: onCardViewClick
    private lateinit var communityPostsViewPagerAdapter: CommunityPostsViewPagerAdapter


    inner class ViewHolder(private val binding: CarSellPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: SellCarPostForMainPage
        fun bind() {
            item = posts[absoluteAdapterPosition]

            loadData()
            manipulateArrows()
            setListeners()
        }

        private fun loadData() {

            binding.priceTV.text = "ფასი: " + item.price.toString() + "$"
            binding.title.text = item.articleHeader
            binding.postDateTV.text = item.createdData.toFormattedDate()

        }

        private fun setListeners() {

            binding.leftArrBTN.setOnClickListener {
                binding.photos.currentItem = binding.photos.currentItem - 1
            }

            binding.rightArrBTN.setOnClickListener {
                binding.photos.currentItem = binding.photos.currentItem + 1
            }

            binding.root.setOnClickListener {
                onCardViewClick.invoke(item.id.toLong())
            }
        }

        private fun manipulateArrows() {
            if (item.photoUrl.isEmpty()) {
                binding.rightArrBTN.hide()
                binding.leftArrBTN.hide()
                if (binding.photos.isVisible) {
                    binding.photos.hide()
                }
            } else {
                if (!binding.photos.isVisible) {
                    binding.photos.show()
                }
                communityPostsViewPagerAdapter =
                    CommunityPostsViewPagerAdapter(item.photoUrl)
                binding.photos.adapter = communityPostsViewPagerAdapter
                binding.photos.setPageTransformer(DepthTransformer)
            }


            binding.photos.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    when {
                        1 == item.photoUrl.size -> {
                            binding.rightArrBTN.hide()
                            binding.leftArrBTN.hide()
                        }
                        position == item.photoUrl.size - 1 -> {
                            binding.rightArrBTN.hide()
                            binding.leftArrBTN.show()
                        }
                        position == 0 -> {
                            binding.rightArrBTN.show()
                            binding.leftArrBTN.hide()
                        }
                        else -> {
                            binding.rightArrBTN.show()
                            binding.leftArrBTN.show()
                        }
                    }
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            CarSellPostItemBinding.inflate(
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