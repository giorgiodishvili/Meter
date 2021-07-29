package com.example.meter.repository.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class RealtimeDbRepImpl @Inject constructor(
    private val firebaseDB: FirebaseDatabase
) : RealtimeDbRep {

//    override fun readFromDB() = firebaseDB.child(firebaseAuth.currentUser?.uid!!)
    override fun createNode(from: String, to: String): DatabaseReference {
        return firebaseDB.getReference("/messages").child(from).child(to)
    }
    override fun createReversedNode(to: String, from: String): DatabaseReference {
        return firebaseDB.getReference("/messages").child(to).child(from)
    }


}