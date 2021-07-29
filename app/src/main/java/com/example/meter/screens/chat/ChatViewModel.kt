package com.example.meter.screens.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.Chat
import com.example.meter.entity.UserDetails
import com.example.meter.entity.community.post.Content
import com.example.meter.entity.push_notification.PushNotificationRequest
import com.example.meter.entity.push_notification.PushNotificationResponse
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseMessagingRepo
import com.example.meter.repository.userInfo.UserInfoRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val userInfo: UserInfoRepositoryImpl,
    private val firebaseMessagingRepo: FirebaseMessagingRepo
) : ViewModel() {

    private var _readUserInfo = MutableLiveData<Resource<UserDetails>>()
    val readUserInfo: LiveData<Resource<UserDetails>> = _readUserInfo


    private val _sendPush = MutableLiveData<Resource<PushNotificationResponse>>()

    val sendPush: LiveData<Resource<PushNotificationResponse>>
        get() = _sendPush

    fun getUserInfo(uid: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val result = userInfo.getUserPersonalInfo(uid)
                _readUserInfo.postValue(result)
            }
        }
    }


    fun sendPush(userId: String, pushNotificationRequest: PushNotificationRequest) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _sendPush.postValue(
                    firebaseMessagingRepo.sendPushNotification(
                        userId, pushNotificationRequest
                    )
                )
            }
        }
    }

}