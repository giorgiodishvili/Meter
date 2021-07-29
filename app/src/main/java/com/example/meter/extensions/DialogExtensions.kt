package com.example.meter.extensions

import android.app.Dialog
import android.view.Window
import android.view.WindowManager
import androidx.viewbinding.ViewBinding

fun Dialog.showDialog(dialogView: Int, binding: ViewBinding? = null) {
    window!!.setBackgroundDrawableResource(android.R.color.transparent)
    window!!.requestFeature(Window.FEATURE_NO_TITLE)
    if (binding != null) {
        setContentView(binding.root)
        binding.root.setMargins(40, 0, 40, 0)
    }
    else
        setContentView(dialogView)
    window!!.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
    window!!.attributes.height = WindowManager.LayoutParams.WRAP_CONTENT


}
