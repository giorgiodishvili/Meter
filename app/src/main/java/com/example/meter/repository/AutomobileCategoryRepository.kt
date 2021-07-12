package com.example.meter.repository

import com.example.meter.entity.AutomobileCategory
import com.example.meter.entity.Model
import retrofit2.Response
import retrofit2.http.Path

interface AutomobileCategoryRepository {
    suspend fun getAllCategories(): Response<List<AutomobileCategory>>
    suspend fun getModelsForMake(make: String) : Response<Model>
}
