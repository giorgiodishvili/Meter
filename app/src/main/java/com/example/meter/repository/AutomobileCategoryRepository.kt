package com.example.meter.repository

import com.example.meter.entity.AutomobileCategory
import retrofit2.Response

interface AutomobileCategoryRepository {
    suspend fun getAllCategories(): Response<List<AutomobileCategory>>
}
