package com.example.meter.network

import com.example.meter.entity.AutomobileCategory
import com.example.meter.entity.Comment
import com.example.meter.entity.UserDetails
import com.example.meter.entity.community.post.Content
import com.example.meter.entity.community.post.PagedPostResponse
import com.example.meter.entity.page.Model
import com.example.meter.entity.page.UploadPhotoResponse
import com.example.meter.entity.push_notification.PushNotificationRequest
import com.example.meter.entity.push_notification.PushNotificationResponse
import com.example.meter.entity.sell.SellCarPost
import com.example.meter.entity.sell.SellCarPostForMainPage
import com.example.meter.entity.user.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    @GET("/category")
    suspend fun getAllCategories(): Response<List<AutomobileCategory>>

    @GET("https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformake/{make}?format=json")
    suspend fun getModelsForMake(@Path("make") make: String): Response<Model>

    @GET("/community/post/search")
    suspend fun searchPosts(
        @Query("title") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<PagedPostResponse<Content>>

    @GET("/api/cars/searchUrl")
    suspend fun searchCar(
        @Query("search") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<PagedPostResponse<SellCarPostForMainPage>>

    @GET("/api/cars/latest")
    suspend fun getLatestPosts(): Response<List<Content>>

    @GET("/user/{uid}")
    suspend fun getUserPersonalInfo(@Path("uid") uid: String): Response<UserDetails>

    @GET("/user/post/community/{uid}")
    suspend fun getUserPosts(@Path("uid") uid: String): Response<List<Content>>

    @GET("/user/post/mainpage/car/{uid}")
    suspend fun getCarsForMainPageByUserId(@Path("uid") uid: String): Response<List<SellCarPostForMainPage>>

    @FormUrlEncoded
    @POST("/user/")
    suspend fun postUserPersonalInfo(
        @Field("id") uid: String,
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("number") number: String,
        @Field("url") url: String,
        @Field("verified") verified: Boolean,
        @Query("token") token: String = ""
    ): Response<UserDetails>

    @GET("/community/post/")
    suspend fun getCommunityPosts(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<PagedPostResponse<Content>>

    @POST("/user/react/like")
    suspend fun createLike(
        @Query("postId") postId: Long,
        @Query("userId") userId: String
    ): Response<Boolean>

    @DELETE("/user/react/dislike")
    suspend fun deleteLike(
        @Query("postId") postId: Long,
        @Query("userId") userId: String
    ): Response<Boolean>

    @GET("/user/likes/{userId}")
    suspend fun getLikedCommentsIDsForUser(
        @Path("userId") uid: String
    ): Response<List<Long>>


    @POST("/community/post/")
    @FormUrlEncoded
    suspend fun addCommunityPost(
        @Field("userId") userId: String,
        @Field("description") description: String,
        @Field("title") title: String
    ): Response<Content>

//    @POST("/photos/upload")
//    @FormUrlEncoded
//    suspend fun uploadPostPhoto(
//        @Query("postId") postId: Int,
//        @Field("file") file: ByteArray
//    ): Response<UploadPhotoResponse>

    @POST("/photos/upload")
    @Multipart
    suspend fun uploadPostPhoto(
        @Part("postId") postId: Long,
        @Part file: List<MultipartBody.Part>
    ): Response<Boolean>

    @POST("/user/list/{userId}")
    suspend fun getUsersForChat(
        @Path("userId") list: String
    ): Response<List<UserDetails>>

    @DELETE("community/post")
    @FormUrlEncoded
    suspend fun deleteCommunityPostById(
        @Path("postId") postId: Long,
    ): Response<Content>

    @PUT("community/post")
    @FormUrlEncoded
    suspend fun updateCommunityPostById(
        @Path("postId") postId: Long,
    ): Response<Content>


    @DELETE("photos/{photoId}")
    @FormUrlEncoded
    suspend fun deletePhotoById(
        @Path("photoId") photoId: Long,
    ): Response<UploadPhotoResponse>

    @GET("community/post/{postId}")
    suspend fun getSingleCommunityPost(@Path("postId") postId: Long): Response<Content>

    @GET("community/post/comments/{postId}")
    suspend fun getComments(@Path("postId") postId: Long): Response<List<Comment>>

    @POST("community/post/comments")
    @FormUrlEncoded
    suspend fun createComment(
        @Query("postId") postId: Long,
        @Query("userId") userId: String,
        @Field("description") description: String
    ): Response<Comment>

    @DELETE("community/post/comments")
    suspend fun deleteComment(
        @Query("postId") postId: Long,
    ): Response<Comment>


    @GET("api/cars/")
    suspend fun getAllSellPostForMainPage(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<PagedPostResponse<SellCarPostForMainPage>>

    @DELETE("api/cars/{carId}")
    suspend fun deleteSellCarPost(
        @Path("carId") carId: Long,
    ): Response<SellCarPost>

    @DELETE("community/post/{postId}")
    suspend fun deleteCommunityPost(
        @Path("postId") postId: Long,
    ): Response<Content>

    @GET("api/cars/{carId}")
    suspend fun getSellCarPost(
        @Path("carId") carId: Long,
    ): Response<SellCarPost>

    @POST("notification/token")
    suspend fun sendPushNotification(
        @Query("userId") userId: String,
        @Body pushNotificationRequest: PushNotificationRequest
    ): Response<PushNotificationResponse>

    @POST("/user/token/save/{userId}/{token}")
    suspend fun saveToken(
        @Path("userId") userId: String,
        @Path("token") token: String
    ): Response<Boolean>


    @DELETE("/user/token/delete/{token}")
    suspend fun deleteToken(
        @Path("token") token: String
    ): Response<Boolean>

    @POST("/user/token/{tokenId}")
    suspend fun saveOnlyToken(
        @Path("tokenId") token: String
    ): Response<String>

    @POST("/user/token/update")
    suspend fun updateOldToken(
        @Query("newToken") newToken: String,
        @Query("oldToken") oldToken: String
    ): Response<String>


    @POST("api/cars/")
    @FormUrlEncoded
    suspend fun createSellCarPost(
        @Query("userId") userId: String?,
        @Field("AUX") AUX: Boolean,
        @Field("address") address: String,
        @Field("airConditioner") airConditioner: Boolean,
        @Field("airBag") airBag: Int,
        @Field("backupTire") backupTire: Boolean,
        @Field("bluetooth") bluetooth: Boolean,
        @Field("boardComputer") boardComputer: Boolean,
        @Field("color") color: String,
        @Field("centralLock") centralLock: Boolean,
        @Field("climateControl") climateControl: Boolean,
        @Field("cylinder") cylinder: Int,
        @Field("description") description: String,
        @Field("disks") disks: Boolean,
        @Field("doors") doors: String,
        @Field("elWindow") elWindow: Boolean,
        @Field("engine") engine: Double,
        @Field("fuelType") fuelType: String,
        @Field("id") id: Int,
        @Field("interiorColor") interiorColor: String,
        @Field("interiorMake") interiorMake: String,
        @Field("seatHead") seatHead: Boolean,
        @Field("manufacturer") manufacturer: String,
        @Field("mileage") mileage: Int,
        @Field("model") model: String,
        @Field("multiWheel") multiWheel: Boolean,
        @Field("navigation") navigation: Boolean,
        @Field("photoUrl") photoUrl: List<String>,
        @Field("price") price: Int,
        @Field("rearViewCamera") rearViewCamera: Boolean,
        @Field("releaseYear") releaseYear: String,
        @Field("signalization") signalization: Boolean,
        @Field("startStopSystem") startStopSystem: Boolean,
        @Field("tires") tires: String,
        @Field("gadacemataKolofi") gadacemataKolofi: String,
        @Field("user:") user: User?,
        @Field("VIN") VIN: String,
        @Field("wheel") wheel: String,
        @Field("cruiseControl") cruiseControl: Boolean,
        @Field("luqi") luqi: Boolean,
        @Field("antiSlide") antiSlide: Boolean,
        @Field("seatMemory") seatMemory: Boolean,
        @Field("sanisleparebi") sanisleparebi: Boolean,
        @Field("techOverview") techOverview: Boolean,
        @Field("hydravlick") hydravlick: Boolean
    ): Response<SellCarPost>

    //    @PUT("/api/cars/latest")
//    suspend fun putUserPersonalInfo(@Field("uid") uid: String): Response<UserDetails>

}