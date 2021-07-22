package com.example.meter.screens.bottom_nav.profile.myposts.commPosts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.meter.entity.community.post.Content
import com.example.meter.network.Resource
import com.example.meter.repository.post.CommunityPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyCommPostsViewModel @Inject constructor(private val communityPostRepository: CommunityPostRepository) : ViewModel() {

    private val _userPosts = MutableLiveData<Resource<PagingData<Content>>>()
    val userPosts: LiveData<Resource<PagingData<Content>>>
        get() = _userPosts

    fun getUserPosts() {

    }


}