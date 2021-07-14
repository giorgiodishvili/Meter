package com.example.shualeduri.extensions

import android.app.Dialog
import android.view.Window
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Dialog.showDialog(binding: ViewBinding) {
    CoroutineScope(Dispatchers.Main).launch {

        window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setContentView(binding.root)
        show()
        delay(4400)
        cancel()
    }

}