package com.example.meter.screens.chat.messagerequests

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.UserDetails
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseMessagingRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ChatRequestsViewModel @Inject constructor(private val messagingRepo: FirebaseMessagingRepoImpl) :
    ViewModel() {

    private var _usersForChat = MutableLiveData<Resource<List<UserDetails>>>()
    val usersForChat: LiveData<Resource<List<UserDetails>>> = _usersForChat

    fun getUsersForChat(list: List<String>) {
        Log.d("childrencound", "successviewmodel")

        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val changedList: String = list.toString().trim()
                val input = changedList.substring(1, changedList.length - 1)
                val result = messagingRepo.getUsersForChat(input)
                _usersForChat.postValue(result)
            }
        }
    }

}