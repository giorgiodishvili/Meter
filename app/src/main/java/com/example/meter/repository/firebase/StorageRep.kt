package com.example.meter.repository.firebase

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask

interface StorageRep {
    fun uploadImage(uri: Uri): UploadTask
    fun getImage(uid: String): Task<Uri>

}