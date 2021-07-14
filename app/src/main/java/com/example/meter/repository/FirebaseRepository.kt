package com.example.meter.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    fun signUpUser(email:String,password:String) = firebaseAuth.createUserWithEmailAndPassword(email,password)
    fun signInUser(email: String,password: String) = firebaseAuth.signInWithEmailAndPassword(email,password)
    fun resetUser(email: String) = firebaseAuth.sendPasswordResetEmail(email)

    fun signInWithGoogle(credential: AuthCredential) = firebaseAuth.signInWithCredential(credential)


}