package com.example.meter.entity

import com.google.gson.annotations.SerializedName

data class SellCarPost(
    val articleHeader: String,
    val id: Int,
    val model: String,

    @SerializedName("photoUrl")
    val photoUrl: List<String>,

    val price: Int,
    val user: Int
)