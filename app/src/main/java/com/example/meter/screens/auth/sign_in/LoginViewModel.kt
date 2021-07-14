package com.example.meter.screens.auth.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import com.example.meter.R
import com.example.meter.repository.FirebaseRepository
import com.example.shualeduri.extensions.showToast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val firebaseAuth: FirebaseRepository) : ViewModel() {

    private var _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> = _loginStatus

    fun loginStart(email: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                firebaseAuth.signInUser(email, password).addOnCompleteListener { process ->
                    if (process.isSuccessful)
                        _loginStatus.postValue(true)
                    else
                        _loginStatus.postValue(false)
                }
            }
        }
    }
}
