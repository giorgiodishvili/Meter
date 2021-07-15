package com.example.meter.repository.post

import com.example.meter.entity.AutomobileCategory
import com.example.meter.entity.PostItem
import retrofit2.Response

interface PostRepository {

    suspend fun searchPosts(query: String): Response<List<PostItem>>
    suspend fun getLatestPosts(): Response<List<PostItem>>

}