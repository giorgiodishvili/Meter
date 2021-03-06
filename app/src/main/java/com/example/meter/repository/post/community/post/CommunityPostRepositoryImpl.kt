package com.example.meter.repository.post.community.post

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.meter.entity.community.post.Content
import com.example.meter.network.ApiService
import com.example.meter.network.Resource
import com.example.meter.paging.source.post.CommunityPostPagingSource
import com.example.meter.paging.source.post.CommunityPostPagingSourceForSearch
import javax.inject.Inject

class CommunityPostRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    CommunityPostRepository {

    companion object {
        private const val NETWORK_PAGE_SIZE = 5
    }

    override fun getCommunityPost(): LiveData<PagingData<Content>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { CommunityPostPagingSource(apiService, NETWORK_PAGE_SIZE) }
        ).liveData
    }

    override suspend fun createLike(postId: Long, userId: String): Resource<Boolean> {
        return try {

            val response = apiService.createLike(postId, userId)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }

    }

    override suspend fun deleteLike(postId: Long, userId: String): Resource<Boolean> {
        return try {

            val response = apiService.deleteLike(postId, userId)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())

            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override suspend fun getLikedCommentsIDsForUser(userId: String): Resource<List<Long>> {
        return try {

            val response = apiService.getLikedCommentsIDsForUser(userId)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override suspend fun uploadPost(
        postId: String,
        description: String,
        title: String
    ): Resource<Content> {
        return try {

            Resource.loading<Content>()
            val response = apiService.addCommunityPost(postId, description, title)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override suspend fun getSinglePost(postId: Long): Resource<Content> {
        return try {

            Resource.loading<Content>()
            val response = apiService.getSingleCommunityPost(postId)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override suspend fun deletePost(postId: Long): Resource<Content> {
        return try {

            Resource.loading<Content>()
            val response = apiService.deleteCommunityPost(postId)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override fun searchPost(keyword: String): LiveData<PagingData<Content>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                CommunityPostPagingSourceForSearch(
                    keyword,
                    apiService,
                    NETWORK_PAGE_SIZE
                )
            }
        ).liveData
    }
}