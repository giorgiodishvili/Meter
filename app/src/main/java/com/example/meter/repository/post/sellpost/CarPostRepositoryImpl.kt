package com.example.meter.repository.post.sellpost

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.meter.entity.sell.SellCarPost
import com.example.meter.entity.sell.SellCarPostForMainPage
import com.example.meter.entity.sell.SellCarPostRequest
import com.example.meter.network.ApiService
import com.example.meter.network.Resource
import com.example.meter.paging.source.car.CarPagingSourceForSearch
import com.example.meter.paging.source.car.CarPostPagingSource
import javax.inject.Inject

class CarPostRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    CarPostRepository {

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    override fun searchPosts(query: StringBuilder): LiveData<PagingData<SellCarPostForMainPage>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                CarPagingSourceForSearch(
                    query.toString(),
                    apiService,
                    NETWORK_PAGE_SIZE
                )
            }
        ).liveData
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

    override suspend fun createSellPost(
        userId: String?,
        sellCarPost: SellCarPostRequest
    ): Resource<SellCarPost> {
        return try {

            val response = apiService.createSellCarPost(
                userId,
                sellCarPost.address.toString(),
                sellCarPost.cylinder!!.toInt(),
                sellCarPost.description.toString(),
                sellCarPost.engine!!.toDouble(),
                sellCarPost.fuelType.toString(),
                sellCarPost.id!!.toInt(),
                sellCarPost.manufacturer.toString(),
                sellCarPost.model.toString(),
                mutableListOf(),
                sellCarPost.price!!.toInt(),
                sellCarPost.releaseYear.toString(),
                sellCarPost.gadacemataKolofi.toString(),
                null,
                sellCarPost.VIN.toString(),
                sellCarPost.wheel_side.toString()
            )
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

    override suspend fun getCarsForMainPageByUserId(uid: String): Resource<List<SellCarPostForMainPage>> {
        return try {
            val response = apiService.getCarsForMainPageByUserId(uid)
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