package com.example.meter.network

import com.example.meter.entity.AutomobileCategory
import com.example.meter.entity.Comment
import com.example.meter.entity.Model
import com.example.meter.entity.UserDetails
import com.example.meter.entity.community.post.CommunityPost
import com.example.meter.entity.community.post.Content
import com.example.meter.entity.community.post.UploadPhotoResponse
import okhttp3.MultipartBody
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
    suspend fun getUserPosts(@Path("uid") uid: String): Response<List<Content>>

    @FormUrlEncoded
    @POST("/user/")
    suspend fun postUserPersonalInfo(
        @Field("id") uid: String,
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("number") number: String,
        @Field("url") url: String,
        @Field("verified") verified: Boolean,
    ): Response<UserDetails>

    @GET("/community/post/")
    suspend fun getCommunityPosts(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<CommunityPost>

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


    //    @PUT("/api/cars/latest")
//    suspend fun putUserPersonalInfo(@Field("uid") uid: String): Response<UserDetails>

}