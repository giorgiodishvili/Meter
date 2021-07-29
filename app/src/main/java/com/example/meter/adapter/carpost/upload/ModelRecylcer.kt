package com.example.meter.adapter.carpost.upload

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.databinding.DropDownItemBinding
import com.example.meter.entity.page.MetaData

typealias onModelClick = (model: String) -> Unit

class ModelRecylcer: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var onModelClick: onModelClick
    private val items = mutableListOf<MetaData>()

    inner class ViewHolder(private val binding: DropDownItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var model: MetaData
        fun bind() {
            model = items[absoluteAdapterPosition]
            binding.textView.text = model.Model_Name
            binding.root.setOnClickListener {
                onModelClick.invoke(model.Model_Name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(DropDownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder ->  holder.bind()
        }
    }

    override fun getItemCount(): Int = items.size

    fun addData(items: MutableList<MetaData>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

}