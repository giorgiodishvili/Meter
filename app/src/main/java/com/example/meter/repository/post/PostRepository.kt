package com.example.meter.repository.post

import com.example.meter.entity.sell.SellCarPostForMainPage
import retrofit2.Response

interface PostRepository {

    suspend fun searchPosts(query: String): Response<List<SellCarPostForMainPage>>
    suspend fun getLatestPosts(): Response<List<SellCarPostForMainPage>>

}