package com.example.meter.entity.sell

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class SellCarPostForMainPage(
    val articleHeader: String,
    val id: Int,
    val model: String,

    @SerializedName("photoUrl")
    val photoUrl: List<String>,

    val price: Int,
    val user: Int,
    val createdData: LocalDateTime
)