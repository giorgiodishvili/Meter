package com.example.meter.screens.auth

import androidx.lifecycle.ViewModel
import com.example.meter.repository.RealtimeDbRepository
import com.example.meter.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainAuthViewModel @Inject constructor(
    private val realtimeDbRepository: RealtimeDbRepository,
    private val firebaseStorage: StorageRepository
): ViewModel() {
}