package com.example.meter.repository.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface FirebaseRep {
    fun getUser(): FirebaseUser?
    fun getUserId(): String?
    fun signOut()

    fun signUpUser(email:String,password:String): Task<AuthResult>
    fun signInUser(email: String,password: String): Task<AuthResult>
    fun resetUser(mail: String): Task<Void>

    fun signInWithGoogle(credential: AuthCredential): Task<AuthResult>
}