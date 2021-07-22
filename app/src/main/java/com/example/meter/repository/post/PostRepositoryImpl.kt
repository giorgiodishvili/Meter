//package com.example.meter.repository.post
//
//import com.example.meter.entity.SellCarPost
//import com.example.meter.network.ApiService
//import retrofit2.Response
//import javax.inject.Inject
//
//class PostRepositoryImpl @Inject constructor(private val apiService: ApiService): PostRepository {
//    override suspend fun searchPosts(query: String): Response<List<SellCarPost>> = apiService.getModelsForMake(query)
//    override suspend fun getLatestPosts(): Response<List<SellCarPost>> =apiService.getAllCategories()
//}