package com.example.meter.repository.automobile

import com.example.meter.entity.AutomobileCategory
import com.example.meter.entity.page.Model
import com.example.meter.network.Resource

interface AutomobileCategoryRepository {
    suspend fun getAllManufacturers(): Resource<List<AutomobileCategory>>
    suspend fun getModelsForMake(make: String): Resource<Model>
}
