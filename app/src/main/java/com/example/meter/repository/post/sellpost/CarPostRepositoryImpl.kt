package com.example.meter.repository.post.sellpost

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.meter.entity.sell.SellCarPost
import com.example.meter.entity.sell.SellCarPostForMainPage
import com.example.meter.network.ApiService
import com.example.meter.network.Resource
import com.example.meter.paging.source.CarPostPagingSource
import javax.inject.Inject

class CarPostRepositoryImpl @Inject constructor(private val apiService: ApiService): CarPostRepository {

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    override fun getSellPostsForMainPage(): LiveData<PagingData<SellCarPostForMainPage>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { CarPostPagingSource(apiService, NETWORK_PAGE_SIZE) }
        ).liveData
    }

    override suspend fun createSellPost(sellCarPost: SellCarPost): Resource<SellCarPost> {
        return try {

            val response = apiService.createSellCarPost(sellCarPost)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override suspend fun deleteSellPost(id: Long): Resource<SellCarPost> {
        return try {

            val response = apiService.deleteSellCarPost(id)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }

    override suspend fun getSellPostById(id: Long): Resource<SellCarPost> {
        return try {

            val response = apiService.getSellCarPost(id)
            if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message())
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
    }
//    override suspend fun searchPosts(query: String): Response<List<SellCarPost>> = apiService.getModelsForMake(query)
//    override suspend fun getLatestPosts(): Response<List<SellCarPost>> =apiService.getAllCategories()
}