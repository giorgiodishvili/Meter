package com.example.meter.repository.firebase

import com.example.meter.entity.push_notification.PushNotificationRequest
import com.example.meter.entity.push_notification.PushNotificationResponse
import com.example.meter.network.Resource

interface FirebaseMessagingRepo {

    suspend fun deleteUserFromToken(
        token: String
    ): Resource<Boolean>


    suspend fun saveToken(
        userId: String,
        token: String
    ): Resource<Boolean>


    suspend fun sendPushNotification(
        userId: String,
        pushNotificationRequest: PushNotificationRequest
    ): Resource<PushNotificationResponse>

    suspend fun saveOnlyToken(
        token: String
    ): Resource<String>

    suspend fun updateOldToken(
        newToken: String,
        oldToken: String
    ): Resource<String>
}