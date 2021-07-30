package com.example.meter.screens.auth.reset

import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ResetViewModel @Inject constructor(private val firebaseAuthImpl: FirebaseRepositoryImpl) :
    ViewModel() {

    private var _resetStatus = MutableLiveData<Boolean>()
    val resetStatus: LiveData<Boolean> = _resetStatus

    fun resetStart(email: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                try {
                    firebaseAuthImpl.resetUser(email).addOnCompleteListener { process ->
                        if (process.isSuccessful)
                            _resetStatus.postValue(true)
                        else
                            _resetStatus.postValue(false)
                    }
                } catch (e: Exception) {
                    d("tagtag", "${e.message}")
                }
            }
        }

    }
}