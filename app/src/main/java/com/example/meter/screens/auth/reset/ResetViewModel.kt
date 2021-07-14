package com.example.meter.screens.auth.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meter.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetViewModel @Inject constructor(private val firebaseAuth: FirebaseRepository) : ViewModel() {

    private var _resetStatus = MutableLiveData<Boolean>()
    val resetStatus: LiveData<Boolean> = _resetStatus

    fun resetStart(email: String) {
        firebaseAuth.resetUser(email).addOnCompleteListener { process ->
            if (process.isSuccessful)
                _resetStatus.postValue(true)
            else
                _resetStatus.postValue(false)
        }
    }
}