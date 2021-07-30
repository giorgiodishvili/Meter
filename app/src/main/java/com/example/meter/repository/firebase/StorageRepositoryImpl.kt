package com.example.meter.repository.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val firebaseStorage: StorageReference,
    private val firebaseAuth: FirebaseAuth
) :
    StorageRep {

    override fun uploadImage(uri: Uri) =
        firebaseStorage.child(firebaseAuth.currentUser?.uid!!).putFile(uri)

    override fun getImage(uid: String) = firebaseStorage.child(uid).downloadUrl

}