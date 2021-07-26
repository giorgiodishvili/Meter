package com.example.meter.adapter.carpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.databinding.CarSellPostItemBinding
import com.example.meter.entity.sell.SellCarPostForMainPage

class CarPostRecyclerAdapter(userId: String?) : PagingDataAdapter<SellCarPostForMainPage, CarPostRecyclerAdapter.ItemHolder>(REPO_COMPARATOR) {
    inner class ItemHolder(private val binding: CarSellPostItemBinding)  : RecyclerView.ViewHolder(binding.root){
        fun onBind() {

        }

    }

    companion object {

        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<SellCarPostForMainPage>() {
            override fun areItemsTheSame(oldItem: SellCarPostForMainPage, newItem: SellCarPostForMainPage): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: SellCarPostForMainPage, newItem: SellCarPostForMainPage): Boolean =
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
