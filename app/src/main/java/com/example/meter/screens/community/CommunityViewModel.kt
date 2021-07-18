package com.example.meter.screens.community

import android.util.Log.i
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.SellCarPost
import com.example.meter.entity.community.post.CommunityPost
import com.example.meter.network.Resource
import com.example.meter.repository.post.CommunityPostRepository
import com.example.meter.repository.post.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(private val communityPostRepository: CommunityPostRepository) : ViewModel() {

    private val _latestPosts = MutableLiveData<Resource<List<CommunityPost>>>()

    val latestPosts: LiveData<Resource<List<CommunityPost>>>
        get() = _latestPosts

     fun getCommunityPosts() = viewModelScope.launch {
//        _latestPosts.postValue(Resource.loading())
//         communityPostRepository.getCommunityPost().let {
//            if (it.isSuccessful) {
//                val body = it.body()
//                i("here","$body")
//                _latestPosts.postValue(Resource.success(body!!))
//            } else {
//                _latestPosts.postValue(Resource.error(it.message().toString()))
//            }
//        }
    }



    fun searchCarsForSale(query: String?) {
        i("SearchWord", query!!)
    }
}