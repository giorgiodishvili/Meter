package com.example.meter.screens.bottom_nav.profile.myposts.commPosts

import androidx.lifecycle.*
import com.example.meter.entity.UserDetails
import com.example.meter.entity.community.post.Content
import com.example.meter.network.Resource
import com.example.meter.repository.userInfo.UserInfoRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyCommPostsViewModel @Inject constructor(
    private val userInfo: UserInfoRepositoryImpl,
    val state: SavedStateHandle
) : ViewModel() {


    private var _readUserPosts = MutableLiveData<Resource<List<Content>>>()
    val readUserPosts: LiveData<Resource<List<Content>>> = _readUserPosts

    private var _readUserInfo = MutableLiveData<Resource<UserDetails>>()
    val readUserInfo: LiveData<Resource<UserDetails>> = _readUserInfo

    private var _userId = MutableLiveData("none")
    val userId: LiveData<String> = _userId

    fun saveUserId(uid: String) {
        _userId.value = uid
    }


    fun getUserPosts(uid: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val result = userInfo.getUserPosts(uid)
                _readUserPosts.postValue(result)
            }
        }
    }

    fun getUserInfo(uid: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val result = userInfo.getUserPersonalInfo(uid)
                _readUserInfo.postValue(result)
            }
        }
    }

}