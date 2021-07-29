package com.example.meter.screens.bottom_nav.addpost.upload.carpost.secondpart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.sell.SellCarPost
import com.example.meter.entity.sell.SellCarPostRequest
import com.example.meter.network.Resource
import com.example.meter.repository.photo.PhotoRepositoryImpl
import com.example.meter.repository.post.sellpost.CarPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class PhotoUploadCarSellViewModel @Inject constructor(
    private val photoRepository: PhotoRepositoryImpl,
    private val carSellRepository: CarPostRepository,
) : ViewModel() {
    private val _postUploaded = MutableLiveData<Resource<SellCarPost>>()
    val postUploaded: LiveData<Resource<SellCarPost>>
        get() = _postUploaded

    private val _photoUploaded = MutableLiveData<Resource<Boolean>>()
    val photoUploaded: LiveData<Resource<Boolean>>
        get() = _photoUploaded

    fun uploadPost(
        userId: String,
        description: String,
        sellCarPost: SellCarPostRequest
    ) {
        sellCarPost.description = description
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _postUploaded.postValue(
                    carSellRepository.createSellPost(
                        userId,
                        sellCarPost
                    )
                )
            }
        }
    }

    fun uploadPhoto(postId: Long, file: List<MultipartBody.Part>) {
        Log.i("File List ", "$file")

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _photoUploaded.postValue(photoRepository.uploadPhoto(postId, file))
            }
        }

    }
}