package com.example.meter.network

import com.example.meter.entity.AutomobileCategory
import com.example.meter.entity.Model
import com.example.meter.entity.UserDetails
import com.example.meter.entity.community.post.CommunityPost
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("/category")
    suspend fun getAllCategories(): Response<List<AutomobileCategory>>

    @GET("https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformake/{make}?format=json")
    suspend fun getModelsForMake(@Path("make") make: String): Response<Model>

//    suspend fun searchPosts(query: String): Response<List<PostItem>>
//
//    @GET("/api/cars/latest")
//    suspend fun getLatestPosts(): Response<List<PostItem>>

    @GET("/user/{uid}")
    suspend fun getUserPersonalInfo(@Path("uid") uid: String): Response<UserDetails>

    @GET("/user/post/community/{uid}")
    suspend fun getUserPosts(@Path("uid") uid: String): Response<CommunityPost>

    @FormUrlEncoded
    @POST("/user/")
    suspend fun postUserPersonalInfo(
        @Field("id") uid: String,
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("number") number: String,
        @Field("verified") verified: Boolean,
    ): Response<UserDetails>

    @GET("/community/post/")
    suspend fun getCommunityPosts(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<CommunityPost>

    //    @PUT("/api/cars/latest")
//    suspend fun putUserPersonalInfo(@Field("uid") uid: String): Response<UserDetails>

}