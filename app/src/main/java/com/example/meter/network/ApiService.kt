package com.example.meter.network

import com.example.meter.entity.AutomobileCategory
import com.example.meter.entity.Model
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/category")
    suspend fun getAllCategories() : Response<List<AutomobileCategory>>

    @GET("https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformake/{make}?format=json")
    suspend fun getModelsForMake(@Path("make") make: String) : Response<Model>
}