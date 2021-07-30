package com.example.meter.screens.bottom_nav.profile.editprofile

import android.net.Uri
import android.util.Log
import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import com.example.meter.entity.UserDetails
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import com.example.meter.repository.firebase.StorageRepositoryImpl
import com.example.meter.repository.userInfo.UserInfoRepositoryImpl
import com.google.firebase.database.DatabaseException
import com.google.firebase.storage.StorageException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseStorageImpl: StorageRepositoryImpl,
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl,
    private val userInfo: UserInfoRepositoryImpl,
) : ViewModel() {

    private var _readUserInfo = MutableLiveData<Resource<UserDetails>>()
    val readUserInfo: LiveData<Resource<UserDetails>> = _readUserInfo

    private var _postUserInfo = MutableLiveData<Resource<UserDetails>>()
    val postUserInfo: LiveData<Resource<UserDetails>> = _postUserInfo

    private var _uploadImageStatus = MutableLiveData<Boolean>()
    val uploadImageStatus: LiveData<Boolean> = _uploadImageStatus

    private var _readImageStatus = MutableLiveData<Uri>()
    val readImageStatus: LiveData<Uri> = _readImageStatus


    fun uploadUserInfo(
        email: String,
        name: String,
        number: String,
        url: String?,
        verified: Boolean,
        uri: Uri? = null,
        uploadWithImage: Boolean = true
    ) {
        if (uploadWithImage) {
            uploadSynchronously(email, name, number, url, verified, uri)
            d("tagtag", "heenoimage viewmodel")
        } else {
            uploadInfoOnly(email, name, number, url.toString(), verified)
        }
    }

    fun loadUserInfo(uid: String, loadWithImage: Boolean = true) {
        if (loadWithImage)
            getDataSynchronously(uid)
        else {
            getDataOnly(uid)
        }
    }

    private fun getDataOnly(uid: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                try {
                    val result = userInfo.getUserPersonalInfo(uid)
                    _readUserInfo.postValue(result)
                } catch (e: DatabaseException) {
                    Log.d("tagtag", "${e.message}")
                }
            }
        }
    }

    private fun getDataSynchronously(uid: String) {
        viewModelScope.launch {
            val getInfo = async {
                withContext(Dispatchers.Default) {
                    try {
                        val result = userInfo.getUserPersonalInfo(uid)
                        _readUserInfo.postValue(result)
                    } catch (e: DatabaseException) {
                        d("tagtag", "${e.message}")
                    }
                }
            }
            val getUserImage = async {
                withContext(Dispatchers.Default) {
                    try {
                        firebaseRepositoryImpl.getUserId()?.let {
                            firebaseStorageImpl.getImage(it).addOnCompleteListener { process ->
                                if (process.isSuccessful) {
                                    _readImageStatus.postValue(process.result)
                                    d("uriuri", "${process.result}")
                                }
                            }
                        }
                    } catch (e: StorageException) {
                        Log.d("tagtag", "${e.message}")
                    }
                }
            }
            val readProcess = listOf(getInfo, getUserImage)
            readProcess.awaitAll()
        }
    }

    private fun uploadSynchronously(
        email: String,
        name: String,
        number: String,
        url: String?,
        verified: Boolean,
        uri: Uri?,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                try {
                    if (uri != null) {
                        firebaseStorageImpl.uploadImage(uri).addOnCompleteListener { process ->
                            if (process.isSuccessful) {
                                _uploadImageStatus.postValue(process.isSuccessful)
                                getImageAndUpload(email, name, number, url, verified)
                            }
                        }
                    } else {
                        uploadInfoOnly(email, name, number, url, verified)
                    }
                } catch (e: StorageException) {
                    d("tagtag", "${e.message}")
                }
            }
        }
    }

    private fun getImageAndUpload(
        email: String,
        name: String,
        number: String,
        url: String?,
        verified: Boolean
    ) {
        if (url == null) {
            firebaseRepositoryImpl.getUserId()?.let {
                firebaseStorageImpl.getImage(it).addOnCompleteListener { process ->
                    if (process.isSuccessful) {
                        uploadInfoOnly(email, name, number, process.result.toString(), verified)
                        _readImageStatus.postValue(process.result)
                        d("uriuri", "${process.result}")
                    }
                }
            }
        } else {
            uploadInfoOnly(email, name, number, url, verified)
        }
    }

    private fun uploadInfoOnly(
        email: String,
        name: String,
        number: String,
        url: String? = "",
        verified: Boolean
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                try {
                    val result =
                        userInfo.postUserPersonalInfo(email, name, number, url.toString(), verified)
                    _postUserInfo.postValue(result)
                } catch (e: HttpException) {
                    Log.d("tagtag", "${e.message}")
                }
            }
        }
    }

}