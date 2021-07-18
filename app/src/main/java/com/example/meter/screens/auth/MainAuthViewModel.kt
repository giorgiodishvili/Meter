package com.example.meter.screens.auth

import androidx.lifecycle.ViewModel
import com.example.meter.repository.firebase.RealtimeDbRepImpl
import com.example.meter.repository.firebase.StorageRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainAuthViewModel @Inject constructor(
    private val realtimeDbRepImpl: RealtimeDbRepImpl,
    private val firebaseStorageImpl: StorageRepositoryImpl
): ViewModel() {
}