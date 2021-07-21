package com.example.meter.screens.bottom_nav.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.meter.entity.community.post.Content
import com.example.meter.network.Resource
import com.example.meter.repository.post.CommunityPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val communityPostRepository: CommunityPostRepository) : ViewModel() {

        private val _latestPosts = MutableLiveData<Resource<PagingData<Content>>>()

        val latestPosts: LiveData<Resource<PagingData<Content>>>
        get() = _latestPosts

        fun getCommunityPosts() : LiveData<PagingData<Content>>{
            return communityPostRepository.getCommunityPost().cachedIn(viewModelScope)
        }



        fun searchCarsForSale(query: String?) {
            Log.i("SearchWord", query!!)
        }
}