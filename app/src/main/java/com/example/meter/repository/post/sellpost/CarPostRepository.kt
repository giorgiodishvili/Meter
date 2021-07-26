package com.example.meter.repository.post.sellpost

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.meter.entity.community.post.Content
import com.example.meter.entity.sell.SellCarPost
import com.example.meter.entity.sell.SellCarPostForMainPage
import com.example.meter.network.Resource
import retrofit2.Response

interface CarPostRepository {

//    suspend fun searchPosts(query: String): Response<List<SellCarPostForMainPage>>
//    suspend fun getLatestPosts(): Response<List<SellCarPostForMainPage>>

    fun getSellPostsForMainPage(): LiveData<PagingData<SellCarPostForMainPage>>
    suspend fun createSellPost(sellCarPost: SellCarPost): Resource<SellCarPost>
    suspend fun deleteSellPost(id: Long): Resource<SellCarPost>
    suspend fun getSellPostById(id: Long): Resource<SellCarPost>
}