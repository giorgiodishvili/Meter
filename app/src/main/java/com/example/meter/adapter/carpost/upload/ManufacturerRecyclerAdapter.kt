package com.example.meter.adapter.carpost.upload

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.databinding.DropDownItemBinding
import com.example.meter.entity.AutomobileCategory

typealias onManufacturerClick = (manufacturer: String) -> Unit

class ManufacturerRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var onManufacturerClick: onManufacturerClick
    private val items = mutableListOf<AutomobileCategory>()

    inner class ViewHolder(private val binding: DropDownItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var model: AutomobileCategory
        fun bind() {
            model = items[absoluteAdapterPosition]
            binding.textView.text = model.name
            binding.root.setOnClickListener {
                onManufacturerClick.invoke(model.name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            DropDownItemBinding.inflate(
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

    override fun getItemCount(): Int = items.size

    fun addData(items: MutableList<AutomobileCategory>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

}