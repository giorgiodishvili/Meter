package com.example.meter.extensions

import android.net.Uri
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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

fun ImageView.loadImg(url: String, center: Boolean = true) {
    if (center) {
        Glide.with(this.context)
            .load(url)
            .circleCrop()
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(8)))
            .placeholder(R.drawable.ic_info_button)
            .error(R.drawable.ic_info_button)
            .into(this)
    } else {
        Glide.with(this.context)
            .load(url)
            .centerCrop()
            .transform(MultiTransformation(CircleCrop(), RoundedCorners(8)))
            .placeholder(R.drawable.ic_info_button)
            .error(R.drawable.ic_info_button)
            .into(this)
    }

}

fun ImageView.loadImgUri(url: Uri?, center: Boolean = false) {

    if (center) {

        Glide.with(this.context)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.ic_dot)
            .error(R.drawable.ic_info_button)
            .into(this)
    } else {

        Glide.with(this.context)
            .load(url)
            .circleCrop()
            .placeholder(R.drawable.ic_dot)
            .error(R.drawable.ic_info_button)
            .into(this)
    }

}

fun View.fade(action: NavDirections? = null) {
    animate().alpha(0f).withEndAction {
        setGone()
        alpha = 1f
        if (action != null) {
            findNavController().navigate(action)
        }
    }
}

fun View.slideDown(duration: Int = 400) {
    show()
    val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
    animate.duration = duration.toLong()
    animate.fillAfter = true
    startAnimation(animate)
    setGone()
}


fun View.slideUp(duration: Int = 650) {
    show()
    val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    startAnimation(animate)
}

