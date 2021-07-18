package com.example.meter.repository.post

import com.example.meter.entity.SellCarPost
import retrofit2.Response

interface PostRepository {

    suspend fun searchPosts(query: String): Response<List<SellCarPost>>
    suspend fun getLatestPosts(): Response<List<SellCarPost>>

}