package com.example.meter.entity.sell

import com.example.meter.entity.user.User

class SellCarPostRequest(
    val address: String?,
    val cylinder: Int?,
    val description: String?,
    val engine: Double?,
    val fuelType: String?,
    val id: Int?,
    val manufacturer: String?,
    val mileage: Int?,
    val model: String?,
    val photoUrl: List<String>?,
    val price: Int?,
    val releaseYear: String?,
    val gadacemataKolofi: String?,
    val user: User?,
    val wheel_side: String,
    val VIN: String?,
)