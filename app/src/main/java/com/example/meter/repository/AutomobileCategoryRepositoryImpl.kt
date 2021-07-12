package com.example.meter.repository

import com.example.meter.entity.AutomobileCategory
import com.example.meter.entity.Model
import com.example.meter.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class AutomobileCategoryRepositoryImpl  @Inject constructor(private val apiService: ApiService) : AutomobileCategoryRepository{
    override suspend fun getAllCategories(): Response<List<AutomobileCategory>> = apiService.getAllCategories()
    override suspend fun getModelsForMake(make: String): Response<Model> = apiService.getModelsForMake(make)
}
