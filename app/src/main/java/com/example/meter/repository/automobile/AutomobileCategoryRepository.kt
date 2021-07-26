package com.example.meter.repository.automobile

import com.example.meter.entity.AutomobileCategory
import com.example.meter.entity.page.Model
import retrofit2.Response

interface AutomobileCategoryRepository {
    suspend fun getAllManufacturers(): Response<List<AutomobileCategory>>
    suspend fun getModelsForMake(make: String): Response<Model>
}
