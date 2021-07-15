package com.example.meter.repository

import android.net.Uri
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class StorageRepository @Inject constructor(private val firebaseStorage: StorageReference) {

    fun uploadImage(file: Uri) = firebaseStorage.putFile(file)
    fun getImage() = firebaseStorage.downloadUrl


}