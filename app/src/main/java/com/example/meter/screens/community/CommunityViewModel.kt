package com.example.meter.screens.community

import android.util.Log.i
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.SellCarPost
import com.example.meter.network.Resource
import com.example.meter.repository.post.CommunityPostRepository
import com.example.meter.repository.post.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(private val communityPostRepository: CommunityPostRepository, private val postsRepository: PostRepository) : ViewModel() {

    private val _latestPosts = MutableLiveData<Resource<List<SellCarPost>>>()

    val latestPosts: LiveData<Resource<List<SellCarPost>>>
        get() = _latestPosts

     fun getCommunityPosts() = viewModelScope.launch {
        _latestPosts.postValue(Resource.loading())
        postsRepository.getLatestPosts().let {
            if (it.isSuccessful) {
                _latestPosts.postValue(Resource.success(it.body()!!))
            } else {
                _latestPosts.postValue(Resource.error(it.message().toString()))
            }
        }
    }



    fun searchCarsForSale(query: String?) {
        i("SearchWord", query!!)
    }
}