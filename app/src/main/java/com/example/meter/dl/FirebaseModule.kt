package com.example.meter.dl

import com.example.meter.repository.firebase.FirebaseMessagingRepo
import com.example.meter.repository.firebase.FirebaseMessagingRepoImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseDB(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    fun provideFirebaseStorage(): StorageReference {
        return FirebaseStorage.getInstance().getReference("User")
    }

    @Provides
    @Singleton
    fun providesFirebaseMessagingRepository(firebaseMessagingRepo: FirebaseMessagingRepoImpl): FirebaseMessagingRepo =
        firebaseMessagingRepo

}