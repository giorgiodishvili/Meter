package com.example.meter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.meter.entity.community.post.Content
import com.example.meter.repository.post.community.post.CommunityPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(private val communityPostRepository: CommunityPostRepository)  : ViewModel() {
    fun getCommunityPosts(): LiveData<PagingData<Content>> {
        return communityPostRepository.getCommunityPost().cachedIn(viewModelScope)
    }
}