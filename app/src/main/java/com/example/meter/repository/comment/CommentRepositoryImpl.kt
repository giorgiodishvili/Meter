package com.example.meter.repository.comment

import android.util.Log.i
import com.example.meter.entity.Comment
import com.example.meter.network.ApiService
import com.example.meter.network.Resource
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    CommentRepository {
    override suspend fun getComments(postId: Long): Resource<List<Comment>> {
        return try {

            val response = apiService.getComments(postId)
            i("getComments","$response")
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override suspend fun createComment(
        postId: Long,
        userId: String,
        description: String
    ): Resource<Comment> {
        return try {

            val response = apiService.createComment(postId, userId, description)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override suspend fun deleteComment(postId: Long): Resource<Comment> {
        return try {

            val response = apiService.deleteComment(postId)
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