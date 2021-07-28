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

//            val response = apiService.createSellCarPost(
//                userId,
//                sellCarPost.AUX,
//                sellCarPost.address,
//                sellCarPost.airConditioner,
//                sellCarPost.airBag,
//                sellCarPost.backupTire,
//                sellCarPost.bluetooth,
//                sellCarPost.boardComputer,
//                sellCarPost.color,
//                sellCarPost.centralLock,
//                sellCarPost.climateControl,
//                sellCarPost.cylinder,
//                sellCarPost.description,
//                sellCarPost.disks,
//                sellCarPost.doors,
//                sellCarPost.elWindow,
//                sellCarPost.engine,
//                sellCarPost.fuelType,
//                sellCarPost.id,
//                sellCarPost.interiorColor,
//                sellCarPost.interiorMake,
//                sellCarPost.seatHead,
//                sellCarPost.manufacturer,
//                sellCarPost.mileage,
//                sellCarPost.model,
//                sellCarPost.multiWheel,
//                sellCarPost.navigation,
//                mutableListOf(),
//                sellCarPost.price,
//                sellCarPost.rearViewCamera,
//                sellCarPost.releaseYear,
//                sellCarPost.signalization,
//                sellCarPost.startStopSystem,
//                sellCarPost.tires,
//                sellCarPost.gadacemataKolofi,
//                null,
//                sellCarPost.VIN,
//                sellCarPost.wheelSide,
//                sellCarPost.cruiseControl,
//                sellCarPost.luqi,
//                sellCarPost.antiSlide,
//                sellCarPost.seatMemory,
//                sellCarPost.sanisleparebi,
//                sellCarPost.techOverview,
//                sellCarPost.hydravlick
//            )
//            if (response.isSuccessful) {
//                Resource.success(response.body()!!)
//            } else {
            //                Resource.error(response.message())

//            }

            Resource.error("ASDASD")

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