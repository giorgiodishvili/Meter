package com.example.meter.utils.transformers

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class AccordionTransformer {
    companion object : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            page.pivotX = if (position < 0.0f) 0.0f else page.width.toFloat()
            page.scaleX = if (position < 0.0f) 1.0f + position else 1.0f - position
        }
    }

}