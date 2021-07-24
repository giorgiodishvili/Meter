package com.example.meter.repository.photo

import com.example.meter.entity.community.post.Content
import com.example.meter.network.Resource
import okhttp3.MultipartBody

interface PhotoRepository {
    suspend fun uploadPhoto(
        postId: Int,
        file: ByteArray
    ): Resource<String>
}