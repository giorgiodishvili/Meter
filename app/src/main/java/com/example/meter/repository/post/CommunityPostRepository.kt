package com.example.meter.repository.post

import com.example.meter.entity.community.post.CommunityPost
import retrofit2.Response

interface CommunityPostRepository {
    suspend fun getCommunityPost(): Response<CommunityPost>

}
