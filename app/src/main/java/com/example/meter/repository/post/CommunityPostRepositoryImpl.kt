package com.example.meter.repository.post

import com.example.meter.entity.community.post.CommunityPost
import com.example.meter.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class CommunityPostRepositoryImpl  @Inject constructor(private val apiService: ApiService): CommunityPostRepository {
    override suspend fun getCommunityPost(): Response<CommunityPost> =
        apiService.getCommunityPosts();
}