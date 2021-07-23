package com.example.meter.paging.loadstate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.R

class LoaderStateAdapter :
    LoadStateAdapter<LoaderStateAdapter.LoaderViewHolder>() {

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        return LoaderViewHolder.getInstance(parent)
    }

    /**
     * view holder class for footer loader and error state handling
     */
    class LoaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            fun getInstance(parent: ViewGroup): LoaderViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view =
                    inflater.inflate(R.layout.item_recyclerview_load_more_layout, parent, false)
                return LoaderViewHolder(view)
            }
        }

        private val motionLayout: ProgressBar = view.findViewById(R.id.progressBar)

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                //TODO Error Handling
            }
            motionLayout.isVisible = loadState is LoadState.Loading
        }
    }
}