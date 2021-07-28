package com.example.meter.repository.firebase

import com.google.firebase.database.DatabaseReference

interface RealtimeDbRep {
//    fun readFromDB(): DatabaseReference
    fun createNode(from: String, to: String): DatabaseReference
    fun createReversedNode(to: String, from: String): DatabaseReference

}