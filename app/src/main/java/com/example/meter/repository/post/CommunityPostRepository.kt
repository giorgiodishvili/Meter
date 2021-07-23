package com.example.meter.repository.post

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.meter.entity.community.post.Content
import com.example.meter.network.Resource

interface CommunityPostRepository {
    fun getCommunityPost(): LiveData<PagingData<Content>>
    suspend fun createLike(postId: Int, userId: String): Resource<Boolean>
    suspend fun deleteLike(postId: Int,userId: String): Resource<Boolean>
    suspend fun getLikedCommentsIDsForUser(userId: String): Resource<List<Long>>

}
