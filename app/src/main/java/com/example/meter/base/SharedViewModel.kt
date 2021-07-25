package com.example.meter.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private var _userId = MutableLiveData("none")
    val userId: LiveData<String> = _userId

    fun saveUserId(uid: String) {
        _userId.value = uid
    }

}