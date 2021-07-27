package com.example.meter.entity.push_notification

data class PushNotificationRequest(
    val data: Map<String,String>,
    val message: String,
    val title: String,
    val token: String,
    val topic: String
)