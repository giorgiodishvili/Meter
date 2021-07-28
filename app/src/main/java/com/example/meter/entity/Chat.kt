package com.example.meter.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Chat(
    val fromUid: String,
    val chatId: String,
    val text: String,
    val sentTime: String,
    val toUid: String
) {
    constructor() : this("", "", "", "", "")
}
