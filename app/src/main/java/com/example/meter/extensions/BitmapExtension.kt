package com.example.meter.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun Uri.toBitmap(context: Context): Bitmap {
    var bitmap: Bitmap? = null
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, this)
        bitmap = ImageDecoder.decodeBitmap(source)
        bitmap.copy(Bitmap.Config.ARGB_8888, true)
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)
    }
}