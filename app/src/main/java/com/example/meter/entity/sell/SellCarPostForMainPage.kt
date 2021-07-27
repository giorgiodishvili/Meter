package com.example.meter.entity.sell

import com.google.gson.annotations.SerializedName

data class SellCarPostForMainPage(
    val articleHeader: String,
    val id: Int,
    val model: String,
    @SerializedName("photoUrl")
    val photoUrl: List<String>,
    val price: Int,
    val user: String,
    val createdData: String,
    val releaseYear: String
)