package com.example.meter.app

import android.app.Application
import android.util.Log
import android.util.Log.i
import android.widget.Toast
import com.example.meter.repository.firebase.FirebaseMessagingRepoImpl
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltAndroidApp
class App  :
    Application() {

    override fun onCreate() {
        super.onCreate()

//        tokenListener()
    }

//    private fun tokenListener() {
//
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
//            token?.let {
//                i("token", it)
//                getSharedPreferences("_", MODE_PRIVATE).edit().putString("token", it).apply()
////                runBlocking {
////                    withContext(Dispatchers.IO) {
////                        firebaseMessagingRepo.saveOnlyToken(it)
////                    }
////                }
//            }
//            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
//        })
//    }

}