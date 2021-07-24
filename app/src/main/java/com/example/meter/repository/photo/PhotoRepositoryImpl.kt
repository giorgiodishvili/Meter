package com.example.meter.repository.photo

import android.util.Log.i
import com.example.meter.network.ApiService
import com.example.meter.network.Resource
import okhttp3.MultipartBody
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    PhotoRepository {
    override suspend fun uploadPhoto(postId: Int, file: ByteArray): Resource<String> {
        return try {

            Resource.loading<String>()
            val response = apiService.uploadPostPhoto(postId, file)
            i("respone", "$response")
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                i("respone adasd", "${response.message()}")

                Resource.error(response.message())
            }
        } catch (e: Exception) {
            i("respone sqwer", "${e.message.toString()}")
            Resource.error(e.message.toString())
        }
    }
}