package com.example.meter.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import android.widget.Toast.makeText

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    makeText(this, text, duration).show()
}

fun Context.makePhoneCall(number: String) : Boolean {
    return try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(intent)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
