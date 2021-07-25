package com.example.meter.extensions

import android.content.Context
import android.widget.Toast
import android.widget.Toast.makeText

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    makeText(this, text, duration).show()
}
