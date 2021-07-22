package com.example.meter.repository.firebase

import android.net.Uri
import com.example.meter.network.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask

interface StorageRep {
    fun uploadImage(uri: Uri): Resource<UploadTask>
    fun getImage(): Resource<Task<Uri>>

}