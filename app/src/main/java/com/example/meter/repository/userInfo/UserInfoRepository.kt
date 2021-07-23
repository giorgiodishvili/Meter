package com.example.meter.repository.userInfo

import com.example.meter.entity.UserDetails
import com.example.meter.entity.community.post.Content
import com.example.meter.network.Resource

interface UserInfoRepository {
    suspend fun postUserPersonalInfo(email: String, name: String, number: String, url: String, verified: Boolean): Resource<UserDetails>
    suspend fun getUserPersonalInfo(uid: String): Resource<UserDetails>
    suspend fun getUserPosts(uid: String): Resource<List<Content>>
}