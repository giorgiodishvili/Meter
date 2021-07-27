package com.example.meter.repository.post.community.post

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.meter.entity.community.post.Content
import com.example.meter.network.Resource

interface CommunityPostRepository {
    fun getCommunityPost(): LiveData<PagingData<Content>>
    suspend fun createLike(postId: Long, userId: String): Resource<Boolean>
    suspend fun deleteLike(postId: Long, userId: String): Resource<Boolean>
    suspend fun getLikedCommentsIDsForUser(userId: String): Resource<List<Long>>
    suspend fun uploadPost(
        postId: String,
        description: String,
        title: String
    ): Resource<Content>

    suspend fun getSinglePost(postId: Long): Resource<Content>
    suspend fun deletePost(postId: Long): Resource<Content>
    fun searchPost(keyword: String): LiveData<PagingData<Content>>
}
