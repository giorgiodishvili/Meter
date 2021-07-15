package com.example.meter.repository.automobile

import com.example.meter.entity.AutomobileCategory
import com.example.meter.entity.Model
import retrofit2.Response
import retrofit2.http.Path

interface AutomobileCategoryRepository {
    suspend fun getAllManufacturers(): Response<List<AutomobileCategory>>
    suspend fun getModelsForMake(make: String) : Response<Model>
}
