package com.example.meter.extensions

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.meter.R

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.setGone() {
    visibility = View.GONE
}

fun ImageView.loadImg(url: String) {
    Glide.with(this.context)
        .load(url)
        .circleCrop()
        .placeholder(R.drawable.ic_dot)
        .error(R.drawable.ic_info_button)
        .into(this)
}

fun ImageView.loadImgUri(url: Uri?) {
    Glide.with(this.context)
        .load(url)
        .circleCrop()
        .placeholder(R.drawable.ic_dot)
        .error(R.drawable.ic_info_button)
        .into(this)
}

fun View.fade(action: NavDirections?=null) {
    alpha = 0f
    animate().setDuration(2250).alpha(1f).withEndAction {
        if (action != null) {
            findNavController().navigate(action)
        }
    }
}