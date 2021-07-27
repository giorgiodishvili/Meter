package com.example.meter.entity.sell

import com.example.meter.entity.user.User

data class SellCarPost(
    val address: String,
    val cylinder: Int,
    val description: String, //
    val engine: Double,
    val fuel_type: String, //
    val id: Int,
    val manufacturer: String,//
    val mileage: Int,//
    val model: String,//
    val photoUrl: List<String>,
    val price: Int,
    val releaseYear: String,//
    val transmission_type: String,//
    val user: User?,
    val vin: String,
    val wheel_side: String,
    val createdData: String,
    val articleHeader: String,
)