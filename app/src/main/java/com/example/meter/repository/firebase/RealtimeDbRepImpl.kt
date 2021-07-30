package com.example.meter.repository.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class RealtimeDbRepImpl @Inject constructor(
    private val firebaseDB: FirebaseDatabase
) : RealtimeDbRep {

    override fun createNode(from: String, to: String): DatabaseReference {
        return firebaseDB.getReference("/messages").child(from).child(to)
    }

    override fun incomingChat(uid: String): DatabaseReference {
        return firebaseDB.getReference("/messages").child(uid)
    }

    override fun createReversedNode(to: String, from: String): DatabaseReference {
        return firebaseDB.getReference("/messages").child(to).child(from)
    }


}