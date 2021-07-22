package com.example.meter.repository.userInfo

import com.example.meter.entity.UserDetails
import com.example.meter.network.ApiService
import com.example.meter.network.Resource
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val postService: ApiService,
    private val firebaseAuth: FirebaseAuth
) : UserInfoRepository {

    override suspend fun postUserPersonalInfo(
        email: String,
        name: String,
        number: String,
        verified: Boolean
    ): Resource<UserDetails> {
        return try {
            val response = postService.postUserPersonalInfo(
                firebaseAuth.currentUser?.uid!!,
                email,
                name,
                number,
                verified
            )
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.errorBody().toString())
            }
        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override suspend fun getUserPersonalInfo(uid: String): Resource<UserDetails> {
        return try {
            val response = postService.getUserPersonalInfo(firebaseAuth.currentUser?.uid!!)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.errorBody().toString())
            }
        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

}