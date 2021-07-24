package com.example.meter.repository.photo

import com.example.meter.entity.community.post.UploadPhotoResponse
import com.example.meter.network.Resource

interface PhotoRepository {
    suspend fun uploadPhoto(
        postId: Int,
        file: ByteArray
    ): Resource<UploadPhotoResponse>
}