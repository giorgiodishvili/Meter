package com.example.meter.repository.comment

import com.example.meter.entity.Comment
import com.example.meter.network.Resource
import retrofit2.http.Field
import retrofit2.http.Query

interface CommentRepository {
    suspend fun getComments(postId: Long) : Resource<List<Comment>>
    suspend fun createComment(
        postId: Long,
        userId: String,
        description: String
    ) : Resource<Comment>
    suspend fun deleteComment(postId: Long) : Resource<Comment>
}