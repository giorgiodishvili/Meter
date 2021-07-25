package com.example.meter.screens.bottom_nav.addpost.upload.communitypost

import android.util.Log.i
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.community.post.Content
import com.example.meter.network.Resource
import com.example.meter.repository.photo.PhotoRepositoryImpl
import com.example.meter.repository.post.community.post.CommunityPostRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
class UploadCommunityPostViewModel @Inject constructor(
    private val photoRepository: PhotoRepositoryImpl,
    private val communityPostRepository: CommunityPostRepositoryImpl
) : ViewModel() {
    private val _postUploaded = MutableLiveData<Resource<Content>>()

    val postUploaded: LiveData<Resource<Content>>
        get() = _postUploaded

    private val _photoUploaded = MutableLiveData<Resource<Boolean>>()

    val photoUploaded: LiveData<Resource<Boolean>>
        get() = _photoUploaded

    fun uploadPost(
        userId: String,
        description: String,
        title: String
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _postUploaded.postValue(
                    communityPostRepository.uploadPost(
                        userId,
                        description,
                        title
                    )
                )

            }
        }

    }

    fun uploadPhoto(postId: Long, file: List<MultipartBody.Part>) {
        i("File List ", "$file")

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _photoUploaded.postValue(photoRepository.uploadPhoto(postId, file))
            }
        }
    }
}