package com.example.meter.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.repository.firebase.FirebaseMessagingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val firebaseMessagingRepo: FirebaseMessagingRepo) :
    ViewModel() {

    private var _userId = MutableLiveData("none")
    val userId: LiveData<String> = _userId

    fun saveUserId(uid: String) {
        _userId.value = uid
    }

    fun saveOnlyToken(token: String) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                firebaseMessagingRepo.saveOnlyToken(token)
            }
        }
    }

    fun deleteUserFromToken(token: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                firebaseMessagingRepo.deleteUserFromToken(token)
            }
        }
    }

    fun saveToken(userId: String, token: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                firebaseMessagingRepo.saveToken(userId, token)
            }
        }
    }
}