package com.example.meter.screens.bottom_nav.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.meter.entity.community.post.Content
import com.example.meter.network.Resource
import com.example.meter.repository.post.community.post.CommunityPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(private val communityPostRepository: CommunityPostRepository) :
    ViewModel() {

    private val _createLikeResponse = MutableLiveData<Resource<Boolean>>()

    val createLikeResponse: LiveData<Resource<Boolean>>
        get() = _createLikeResponse

    private val _dislikeResponse = MutableLiveData<Resource<Boolean>>()

    val dislikeResponse: LiveData<Resource<Boolean>>
        get() = _dislikeResponse

    fun getCommunityPosts(): LiveData<PagingData<Content>> {
        return communityPostRepository.getCommunityPost().cachedIn(viewModelScope)
    }

    fun createLike(postId: Int, userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _createLikeResponse.postValue(communityPostRepository.createLike(postId, userId))
            }
        }
    }

    fun dislikePost(postId: Int, userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _dislikeResponse.postValue(communityPostRepository.deleteLike(postId, userId))
            }
        }
    }


    fun searchCarsForSale(query: String?) {
        Log.i("SearchWord", query!!)
    }
}