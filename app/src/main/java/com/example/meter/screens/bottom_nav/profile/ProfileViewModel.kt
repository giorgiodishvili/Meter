package com.example.meter.screens.bottom_nav.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import com.example.meter.entity.UserDetails
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.StorageRepositoryImpl
import com.example.meter.repository.userInfo.UserInfoRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseException
import com.google.firebase.storage.StorageException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseStorageImpl: StorageRepositoryImpl,
    private val userInfo: UserInfoRepositoryImpl,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private var _readUserInfo = MutableLiveData<Resource<UserDetails>>()
    val readUserInfo: LiveData<Resource<UserDetails>> = _readUserInfo

    private var _postUserInfo = MutableLiveData<Resource<UserDetails>>()
    val postUserInfo: LiveData<Resource<UserDetails>> = _postUserInfo

    private var _uploadImageStatus = MutableLiveData<Boolean>()
    val uploadImageStatus: LiveData<Boolean> = _uploadImageStatus

    private var _readImageStatus = MutableLiveData<Uri>()
    val readImageStatus: LiveData<Uri> = _readImageStatus


    fun uploadUserInfo(email: String, name: String, number: String, verified: Boolean, uri: Uri?=null, uploadBoth: Boolean=true) {
        if (uploadBoth) {
            viewModelScope.launch {
                val infoPost = async {
                    withContext(Dispatchers.Default) {
                        try {
                            val result = userInfo.postUserPersonalInfo(email, name, number, verified)
                            _postUserInfo.postValue(result)
                        } catch (e: HttpException) {
                            Log.d("tagtag", "${e.message}")
                        }
                    }
                }
                val imagePost = async {
                    withContext(Dispatchers.Default) {
                        try {
                            uri?.let { uri ->
                                firebaseStorageImpl.uploadImage(uri).addOnCompleteListener { process ->
                                    _uploadImageStatus.postValue(process.isSuccessful)
                                }
                            }
                        } catch (e: StorageException) {
                            Log.d("tagtag", "${e.message}")
                        }
                    }
                }
                val writeProcess = listOf(infoPost, imagePost)
                writeProcess.awaitAll()
            }
        } else {
            viewModelScope.launch {
                    withContext(Dispatchers.Default) {
                        try {
                            val result = userInfo.postUserPersonalInfo(email, name, number, verified)
                            _postUserInfo.postValue(result)
                        } catch (e: HttpException) {
                            Log.d("tagtag", "${e.message}")
                        }
                }
            }
        }

    }

    fun loadUserInfo() {
        viewModelScope.launch {
            val getInfo = async {
                withContext(Dispatchers.Default) {
                    try {
                        val result = userInfo.getUserPersonalInfo(firebaseAuth.currentUser?.uid!!)
                        _readUserInfo.postValue(result)
                    } catch (e: DatabaseException) {
                        Log.d("tagtag", "${e.message}")
                    }
                }
            }
            val getUserImage = async {
                withContext(Dispatchers.Default) {
                    try {
                        firebaseStorageImpl.getImage().addOnCompleteListener { process ->
                            if (process.isSuccessful)
                                _readImageStatus.postValue(process.result)
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

}