package com.example.meter.repository

import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class RealtimeDbRepository @Inject constructor(private val firebaseDB: DatabaseReference) {

    fun addToDb(uid: String, value: Any) = firebaseDB.child(uid).setValue(value)
    fun readFromDB(uid: String) = firebaseDB.child(uid)


}