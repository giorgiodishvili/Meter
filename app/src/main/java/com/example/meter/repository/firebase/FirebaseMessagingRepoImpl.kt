package com.example.meter.repository.firebase

import com.example.meter.entity.UserDetails
import com.example.meter.entity.push_notification.PushNotificationRequest
import com.example.meter.entity.push_notification.PushNotificationResponse
import com.example.meter.network.ApiService
import com.example.meter.network.Resource
import javax.inject.Inject

class FirebaseMessagingRepoImpl @Inject constructor(private val apiService: ApiService) :
    FirebaseMessagingRepo {
    override suspend fun deleteUserFromToken(token: String): Resource<Boolean> {
        return try {

            val response = apiService.deleteToken(token)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override suspend fun saveToken(userId: String, token: String): Resource<Boolean> {
        return try {

            val response = apiService.saveToken(userId, token)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override suspend fun sendPushNotification(
        userId: String,
        pushNotificationRequest: PushNotificationRequest
    ): Resource<PushNotificationResponse> {
        return try {

            val response = apiService.sendPushNotification(userId, pushNotificationRequest)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override suspend fun saveOnlyToken(token: String): Resource<String> {
        return try {

            val response = apiService.saveOnlyToken(token)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override suspend fun updateOldToken(newToken: String, oldToken: String): Resource<String> {
        return try {

            val response = apiService.updateOldToken(newToken, oldToken)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override suspend fun getUsersForChat(list: String): Resource<List<UserDetails>> {
        return try {
            val response = apiService.getUsersForChat(list)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }
        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }
}