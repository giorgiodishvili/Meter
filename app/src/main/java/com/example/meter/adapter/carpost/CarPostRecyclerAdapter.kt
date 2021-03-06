package com.example.meter.adapter.carpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.meter.adapter.communitypost.main.CommunityPostsViewPagerAdapter
import com.example.meter.databinding.CarSellPostItemBinding
import com.example.meter.entity.sell.SellCarPostForMainPage
import com.example.meter.extensions.hide
import com.example.meter.extensions.show
import com.example.meter.extensions.toFormattedDate
import com.example.meter.utils.transformers.DepthTransformer

typealias onCallClick = (uid: String) -> Unit

class CarPostRecyclerAdapter(
    private val postClick: (postId: Long) -> Unit
) :
    PagingDataAdapter<SellCarPostForMainPage, CarPostRecyclerAdapter.ItemHolder>(REPO_COMPARATOR) {

    lateinit var onCallClick: onCallClick

    inner class ItemHolder(private val binding: CarSellPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: SellCarPostForMainPage
        private lateinit var communityPostsViewPagerAdapter: CommunityPostsViewPagerAdapter


        fun onBind() {
            item = getItem(absoluteAdapterPosition)!!
            binding.priceTV.text = "$${item.price}"
            binding.title.text = item.articleHeader
            binding.postDateTV.text = item.createdData.toFormattedDate()
            binding.yearTV.text = item.releaseYear + " წელი"
            manipulateArrows()
            setListeners(item)
        }

        private fun setListeners(data: SellCarPostForMainPage) {

            binding.callButton.setOnClickListener {
                onCallClick.invoke(data.user)
            }


            binding.leftArrBTN.setOnClickListener {
                binding.photos.currentItem = binding.photos.currentItem - 1
            }

            binding.rightArrBTN.setOnClickListener {
                binding.photos.currentItem = binding.photos.currentItem + 1
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
                    CommunityPostsViewPagerAdapter(item.photoUrl!!)
                binding.photos.adapter = communityPostsViewPagerAdapter
                binding.photos.setPageTransformer(DepthTransformer)

                communityPostsViewPagerAdapter.onCardViewClick = {
                    postClick.invoke(item.id.toLong())
                }
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

    companion object {

        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<SellCarPostForMainPage>() {
            override fun areItemsTheSame(
                oldItem: SellCarPostForMainPage,
                newItem: SellCarPostForMainPage
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: SellCarPostForMainPage,
                newItem: SellCarPostForMainPage
            ): Boolean =
                oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            CarSellPostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}
