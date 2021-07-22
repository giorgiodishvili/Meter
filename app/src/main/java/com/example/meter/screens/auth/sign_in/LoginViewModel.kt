package com.example.meter.screens.auth.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val firebaseAuthImpl: FirebaseRepositoryImpl) :
    ViewModel() {

    private var _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> = _loginStatus

    private var _loginGoogleStatus = MutableLiveData<Boolean>()
    val loginGoogleStatus: LiveData<Boolean> = _loginGoogleStatus


    fun loginStart(email: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                firebaseAuthImpl.signInUser(email, password).addOnCompleteListener { process ->
                    if (process.isSuccessful)
                        _loginStatus.postValue(true)
                    else
                        _loginStatus.postValue(false)
                }
            }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuthImpl.signInWithGoogle(credential).addOnCompleteListener { process ->
                    if (process.isSuccessful)
                        _loginGoogleStatus.postValue(true)
                    else
                        _loginGoogleStatus.postValue(false)
                }
            }
        }
    }
}
