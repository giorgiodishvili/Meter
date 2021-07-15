package com.example.meter.repository.post

import com.example.meter.entity.PostItem
import com.example.meter.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(private val apiService: ApiService): PostRepository {
    override suspend fun searchPosts(query: String): Response<List<PostItem>> = apiService.searchPosts(query)
    override suspend fun getLatestPosts(): Response<List<PostItem>> =apiService.getLatestPosts()
}