package com.example.meter.screens.splash

import androidx.lifecycle.ViewModel
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(firebaseRepositoryImpl: FirebaseRepositoryImpl) : ViewModel() {

}