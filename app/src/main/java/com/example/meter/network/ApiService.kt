package com.example.meter.network

import com.example.meter.entity.AutomobileCategory
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/category")
    suspend fun getAllCategories() : Response<List<AutomobileCategory>>
}