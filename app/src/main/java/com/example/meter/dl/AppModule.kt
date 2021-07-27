package com.example.meter.dl

import androidx.viewbinding.BuildConfig
import com.example.meter.network.ApiService
import com.example.meter.repository.automobile.AutomobileCategoryRepository
import com.example.meter.repository.automobile.AutomobileCategoryRepositoryImpl
import com.example.meter.repository.comment.CommentRepository
import com.example.meter.repository.comment.CommentRepositoryImpl
import com.example.meter.repository.photo.PhotoRepository
import com.example.meter.repository.photo.PhotoRepositoryImpl
import com.example.meter.repository.post.community.post.CommunityPostRepository
import com.example.meter.repository.post.community.post.CommunityPostRepositoryImpl
import com.example.meter.repository.post.sellpost.CarPostRepository
import com.example.meter.repository.post.sellpost.CarPostRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = "https://automobile-api.herokuapp.com"

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun automobileCategoryRepository(automobileCategoryRepo: AutomobileCategoryRepositoryImpl): AutomobileCategoryRepository =
        automobileCategoryRepo

    //
    @Provides
    @Singleton
    fun provideCarPostRepository(carPostRepositoryRepo: CarPostRepositoryImpl): CarPostRepository =
        carPostRepositoryRepo

    @Provides
    @Singleton
    fun provideCommunityPostRepository(communityPostRepository: CommunityPostRepositoryImpl): CommunityPostRepository =
        communityPostRepository

    @Provides
    @Singleton
    fun providesPhotoRepository(photoRepository: PhotoRepositoryImpl): PhotoRepository =
        photoRepository

    @Provides
    @Singleton
    fun providesCommentRepository(commentRepository: CommentRepositoryImpl): CommentRepository =
        commentRepository

}