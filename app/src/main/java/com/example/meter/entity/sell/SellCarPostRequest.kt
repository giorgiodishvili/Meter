package com.example.meter.entity.sell

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SellCarPostRequest(
    val address: String?,
    val cylinder: Int?,
    var description: String?,
    val engine: Double?,
    val fuelType: String?,
    val id: Int?,
    val manufacturer: String?,
    val model: String?,
    val photoUrl: List<String>?,
    val price: Int?,
    val releaseYear: String?,
    val gadacemataKolofi: String?,
    val user: String?,
    val wheel_side: String?,
    val VIN: String?,
) : Parcelable