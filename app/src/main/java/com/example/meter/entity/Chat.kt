package com.example.meter.entity

data class Chat(
    val fromUid: String,
    val chatId: String,
    val text: String,
    val sentTime: String,
    val toUid: String
) {
    constructor() : this("", "", "", "", "")
}
