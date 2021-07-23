package com.example.meter.screens.bottom_nav.profile.myposts.commPosts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.UserDetails
import com.example.meter.entity.community.post.MyPost
import com.example.meter.network.Resource
import com.example.meter.repository.userInfo.UserInfoRepositoryImpl
import com.google.firebase.database.DatabaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyCommPostsViewModel @Inject constructor(private val userInfo: UserInfoRepositoryImpl) : ViewModel() {


    private var _readUserPosts = MutableLiveData<Resource<List<MyPost>>>()
    val readUserPosts: LiveData<Resource<List<MyPost>>> = _readUserPosts

    private var _readUserInfo = MutableLiveData<Resource<UserDetails>>()
    val readUserInfo: LiveData<Resource<UserDetails>> = _readUserInfo

    fun getUserPosts(uid: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                try {
                    val result = userInfo.getUserPosts(uid)
                    _readUserPosts.postValue(result)
                } catch (e: DatabaseException) {
                    Log.d("tagtag", "${e.message}")
                }
            }
        }
    }

    fun getUserInfo(uid: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                try {
                    val result = userInfo.getUserPersonalInfo(uid)
                    _readUserInfo.postValue(result)
                } catch (e: DatabaseException) {
                    Log.d("tagtag", "${e.message}")
                }
            }

        }
    }


}