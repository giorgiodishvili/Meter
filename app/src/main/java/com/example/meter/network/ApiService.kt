package com.example.meter.network

import com.example.meter.entity.AutomobileCategory
import com.example.meter.entity.Model
import com.example.meter.entity.SellCarPost
import com.example.meter.entity.community.post.CommunityPost
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/category")
    suspend fun getAllCategories() : Response<List<AutomobileCategory>>

    @GET("https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformake/{make}?format=json")
    suspend fun getModelsForMake(@Path("make") make: String) : Response<Model>

    suspend fun searchPosts(query: String): Response<List<SellCarPost>>

    @GET("/api/cars/latest")
    suspend fun getLatestPosts(): Response<List<SellCarPost>>

    @GET("/community/post/")
    suspend fun getCommunityPosts(): Response<CommunityPost>
}