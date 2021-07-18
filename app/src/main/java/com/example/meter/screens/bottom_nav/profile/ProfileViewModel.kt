package com.example.meter.screens.bottom_nav.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.UserDetails
import com.example.meter.repository.RealtimeDbRepository
import com.example.meter.repository.StorageRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val realtimeDbRepository: RealtimeDbRepository,
    private val firebaseStorage: StorageRepository
) : ViewModel() {

    private var _writeInDbStatus = MutableLiveData<Boolean>()
    val writeInDbStatus: LiveData<Boolean> = _writeInDbStatus

    private var _readFormDbStatus = MutableLiveData<UserDetails?>()
    val readFormDbStatus: LiveData<UserDetails?> = _readFormDbStatus

    private var _uploadImageStatus = MutableLiveData<Boolean>()
    val uploadImageStatus: LiveData<Boolean> = _uploadImageStatus

    private var _readImageStatus = MutableLiveData<Uri>()
    val readImageStatus: LiveData<Uri> = _readImageStatus


    fun uploadUserInfo(uid: String, model: UserDetails, uri: Uri) {
        viewModelScope.launch {
            val infoPost = async {
                withContext(Dispatchers.Default) {
                    try {
                        realtimeDbRepository.addToDb(uid, model).addOnCompleteListener { process ->
                            _writeInDbStatus.postValue(process.isSuccessful)
                        }
                    } catch (e: DatabaseException) {
                        Log.d("tagtag", "${e.message}")
                    }
                }
            }
            val imagePost = async {
                withContext(Dispatchers.Default) {
                    try {
                        firebaseStorage.uploadImage(uri).addOnCompleteListener { process ->
                            _uploadImageStatus.postValue(process.isSuccessful)
                        }
                    } catch (e: StorageException) {
                        Log.d("tagtag", "${e.message}")
                    }
                }
            }
            val writeProcess = listOf(infoPost, imagePost)
            writeProcess.awaitAll()

        }
    }

    fun readFromDb(uid: String) {
        viewModelScope.launch {
            val getInfo = async {
                withContext(Dispatchers.Default) {
                    realtimeDbRepository.readFromDB(uid).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            val result = snapshot.getValue(UserDetails::class.java)
                            if (result != null)
                                _readFormDbStatus.postValue(result)
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Log.d("tagtag", "$error")
                        }
                    })
                }
            }
            val getUserImage = async {
                withContext(Dispatchers.Default) {
                    try {
                        firebaseStorage.getImage().addOnCompleteListener { proccess ->
                            if (proccess.isSuccessful)
                                _readImageStatus.postValue(proccess.result)
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