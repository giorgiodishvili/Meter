package com.example.meter.screens.bottom_nav.market.singlepost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.community.post.Content
import com.example.meter.entity.sell.SellCarPost
import com.example.meter.network.Resource
import com.example.meter.repository.post.sellpost.CarPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SingleCarSellPostViewModel @Inject constructor(
    private val carPostRepository: CarPostRepository
) : ViewModel() {

    private val _deletePost = MutableLiveData<Resource<SellCarPost>>()

    val deletePost: LiveData<Resource<SellCarPost>>
        get() = _deletePost

    private val _post = MutableLiveData<Resource<SellCarPost>>()

    val post: LiveData<Resource<SellCarPost>>
        get() = _post


    fun getPost(postId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _post.postValue(carPostRepository.getSellPostById(postId))
            }
        }
    }

    fun deletePost(postId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _deletePost.postValue(
                    carPostRepository.deleteSellPost(
                        postId.toLong()
                    )
                )
            }
        }
    }

}