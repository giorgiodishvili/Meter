package com.example.meter.screens.bottom_nav.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.meter.entity.UserDetails
import com.example.meter.entity.community.post.Content
import com.example.meter.network.Resource
import com.example.meter.repository.post.community.post.CommunityPostRepository
import com.example.meter.repository.userInfo.UserInfoRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityPostRepository: CommunityPostRepository,
    private val userInfo: UserInfoRepositoryImpl
) :
    ViewModel() {

    private val _createLikeResponse = MutableLiveData<Resource<Boolean>>()

    val createLikeResponse: LiveData<Resource<Boolean>>
        get() = _createLikeResponse

    private var _loading = MutableLiveData<Boolean>().apply {
        true
    }
    var loading: LiveData<Boolean> = _loading

    private var _readUserInfo = MutableLiveData<Resource<UserDetails>>()
    val readUserInfo: LiveData<Resource<UserDetails>> = _readUserInfo

    private val _dislikeResponse = MutableLiveData<Resource<Boolean>>()

    val dislikeResponse: LiveData<Resource<Boolean>>
        get() = _dislikeResponse

    fun getCommunityPosts(): LiveData<PagingData<Content>> {
        return communityPostRepository.getCommunityPost().cachedIn(viewModelScope)
    }

    fun createLike(postId: Long, userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _createLikeResponse.postValue(communityPostRepository.createLike(postId, userId))
            }
        }
    }

    fun dislikePost(postId: Long, userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _dislikeResponse.postValue(communityPostRepository.deleteLike(postId, userId))
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

    fun searchPost(keyWord: String): LiveData<PagingData<Content>> {
        return communityPostRepository.searchPost(keyWord).cachedIn(viewModelScope)
    }


    fun searchCarsForSale(query: String?) {
        Log.i("SearchWord", query!!)
    }
}