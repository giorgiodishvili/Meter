package com.example.meter.screens.auth.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.repository.FirebaseRepository
import com.example.shualeduri.extensions.showToast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val firebaseAuth: FirebaseRepository) : ViewModel() {

    private var _registerStatus = MutableLiveData<Boolean>()
    val registerStatus: LiveData<Boolean> = _registerStatus

    fun registerStart(email: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                firebaseAuth.signUpUser(email, password).addOnCompleteListener { process ->
                    if (process.isSuccessful)
                        _registerStatus.postValue(true)
                    else
                        _registerStatus.postValue(false)
                }
            }
        }
    }
}