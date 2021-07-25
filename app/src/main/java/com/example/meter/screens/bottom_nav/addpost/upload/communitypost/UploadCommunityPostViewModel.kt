package com.example.meter.screens.bottom_nav.addpost.upload.communitypost

import android.util.Log
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
import javax.inject.Inject


@HiltViewModel
class UploadCommunityPostViewModel @Inject constructor(
    private val photoRepository: PhotoRepositoryImpl,
    private val communityPostRepository: CommunityPostRepositoryImpl
) : ViewModel() {

    private val _postUploaded = MutableLiveData<Resource<String>>()
    val postUploaded: LiveData<Resource<String>>
        get() = _postUploaded

    private var _loading = MutableLiveData<Boolean>().apply { true }
    var loading: LiveData<Boolean> = _loading



    fun uploadPost(
        userId: String,
        description: String,
        title: String,
        file: MutableList<ByteArray>
    ) {
        viewModelScope.launch {
            lateinit var uploadPost: Resource<Content>
            withContext(Dispatchers.IO) {
                uploadPost = communityPostRepository.uploadPost(userId, description, title)
                when (uploadPost.status) {
                    Resource.Status.ERROR -> Log.i("degee", "$uploadPost")

                    Resource.Status.SUCCESS -> uploadPost.data?.let { content ->
                        file.map {
                            i("conentnID", "${content.id}")
                            photoRepository.uploadPhoto(content.id, it)
                        }
                    }
                    Resource.Status.LOADING -> Log.i("debugee", "LOADING")
                }
            }
        }
    }

}

