package com.example.meter.repository.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

interface RealtimeDbRep {
    fun addToDb(value: Any): Task<Void>
    fun readFromDB(): DatabaseReference
}