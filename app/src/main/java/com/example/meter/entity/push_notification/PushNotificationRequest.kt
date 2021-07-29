package com.example.meter.entity.push_notification

import com.google.gson.annotations.SerializedName

data class PushNotificationRequest(
    val data: Map<String, String?>
)