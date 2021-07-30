package com.example.meter.screens.bottom_nav.addpost.upload.carpost.firstpart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.sell.SellCarPost
import com.example.meter.entity.sell.SellCarPostRequest
import com.example.meter.network.Resource
import com.example.meter.repository.post.sellpost.CarPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UploadCarSellPostViewModel @Inject constructor(private val carPostRepository: CarPostRepository) :
    ViewModel() {

    private val _sellPost = MutableLiveData<Resource<SellCarPost>>()

    val sellPost: LiveData<Resource<SellCarPost>>
        get() = _sellPost

    fun createSellPost(userId: String?, sellCarPost: SellCarPostRequest) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _sellPost.postValue(
                    carPostRepository.createSellPost(userId, sellCarPost)
                )
            }
        }
    }

}