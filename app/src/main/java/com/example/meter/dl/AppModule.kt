package com.example.meter.dl

import androidx.viewbinding.BuildConfig
import com.example.meter.network.ApiService
import com.example.meter.repository.automobile.AutomobileCategoryRepository
import com.example.meter.repository.automobile.AutomobileCategoryRepositoryImpl
import com.example.meter.repository.post.CommunityPostRepository
import com.example.meter.repository.post.CommunityPostRepositoryImpl
import com.example.meter.repository.post.PostRepository
import com.example.meter.repository.post.PostRepositoryImpl
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
object AppModule{

    @Provides
    fun provideBaseUrl() = "https://automobile-api.herokuapp.com"

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }else{
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun automobileCategoryRepository(automobileCategoryRepo: AutomobileCategoryRepositoryImpl): AutomobileCategoryRepository = automobileCategoryRepo


    @Provides
    @Singleton
    fun providePostRepository(postRepository: PostRepositoryImpl): PostRepository = postRepository

    @Provides
    @Singleton
    fun provideCommunityPostRepository(communityPostRepository: CommunityPostRepositoryImpl): CommunityPostRepository = communityPostRepository

}