package com.example.meter.repository.firebase

import com.example.meter.entity.push_notification.PushNotificationRequest
import com.example.meter.entity.push_notification.PushNotificationResponse
import com.example.meter.network.Resource
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Query

interface FirebaseMessagingRepo {

    suspend fun deleteToken(
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
}