package com.example.meter.repository.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class RealtimeDbRepImpl @Inject constructor(
    private val firebaseDB: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) :
    RealtimeDbRep {

    override fun addToDb(value: Any) =
        firebaseDB.child(firebaseAuth.currentUser?.uid!!).setValue(value)

    override fun readFromDB() = firebaseDB.child(firebaseAuth.currentUser?.uid!!)


}