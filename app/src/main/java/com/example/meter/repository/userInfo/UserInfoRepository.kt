package com.example.meter.repository.userInfo

import com.example.meter.entity.UserDetails
import com.example.meter.network.Resource

interface UserInfoRepository {
    suspend fun postUserPersonalInfo(
        email: String,
        name: String,
        number: String,
        verified: Boolean
    ): Resource<UserDetails>

    suspend fun getUserPersonalInfo(uid: String): Resource<UserDetails>
}