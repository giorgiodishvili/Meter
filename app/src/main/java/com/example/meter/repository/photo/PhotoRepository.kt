package com.example.meter.repository.photo

import com.example.meter.network.Resource
import okhttp3.MultipartBody

interface PhotoRepository {
    suspend fun uploadPhoto(
        postId: Long,
        file: List<MultipartBody.Part>
    ): Resource<Boolean>
}