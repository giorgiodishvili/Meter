package com.example.meter.extensions

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import java.io.File


fun Uri.toFile(context: Context): File? {
    if (this.path == null) {
        return null
    }
    var realPath = String()
    val databaseUri: Uri
    val selection: String?
    val selectionArgs: Array<String>?
    if (this.path!!.contains("/document/image:")) {
        databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        selection = "_id=?"
        selectionArgs = arrayOf(DocumentsContract.getDocumentId(this).split(":")[1])
    } else {
        databaseUri = this
        selection = null
        selectionArgs = null
    }
    try {
        val column = "_data"
        val projection = arrayOf(column)
        val cursor = context.contentResolver.query(
            databaseUri,
            projection,
            selection,
            selectionArgs,
            null
        )
        cursor.let {
            if (it!!.moveToFirst()) {
                val columnIndex = cursor!!.getColumnIndexOrThrow(column)
                realPath = cursor.getString(columnIndex)
            }
            cursor!!.close()
        }
    } catch (e: Exception) {
        Log.i("GetFileUri Exception:", e.message ?: "")
    }
    val path = if (realPath.isNotEmpty()) realPath else {
        when {
            this.path!!.contains("/document/raw:") -> this.path!!.replace(
                "/document/raw:",
                ""
            )
            this.path!!.contains("/document/primary:") -> this.path!!.replace(
                "/document/primary:",
                "/storage/emulated/0/"
            )
            else -> return null
        }
    }
    return File(path)
}