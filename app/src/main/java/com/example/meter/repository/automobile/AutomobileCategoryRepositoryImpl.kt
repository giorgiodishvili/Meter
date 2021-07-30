package com.example.meter.repository.automobile

import android.util.Log
import com.example.meter.entity.AutomobileCategory
import com.example.meter.entity.page.Model
import com.example.meter.network.ApiService
import com.example.meter.network.Resource
import javax.inject.Inject

class AutomobileCategoryRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    AutomobileCategoryRepository {
    override suspend fun getAllManufacturers(): Resource<List<AutomobileCategory>> {
        return try {

            val response = apiService.getAllCategories()
            Log.i("getComments", "$response")
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }

    }

    override suspend fun getModelsForMake(make: String): Resource<Model> {
        return try {
            val response = apiService.getModelsForMake(make)
            Log.i("getComments", "$response")
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }

    }
}
