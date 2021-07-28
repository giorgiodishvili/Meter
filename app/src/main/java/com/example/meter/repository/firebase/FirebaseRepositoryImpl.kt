package com.example.meter.repository.firebase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject


class FirebaseRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    FirebaseRep {

    override fun getUser() = firebaseAuth.currentUser
    override fun getUserId() = firebaseAuth.currentUser?.uid
    override fun signOut() = firebaseAuth.signOut()

    override fun signUpUser(email: String, password: String) =
        firebaseAuth.createUserWithEmailAndPassword(email, password)

    override fun signInUser(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)

    override fun resetUser(mail: String) = firebaseAuth.sendPasswordResetEmail(mail)

    override fun signInWithGoogle(credential: AuthCredential) =
        firebaseAuth.signInWithCredential(credential)

}