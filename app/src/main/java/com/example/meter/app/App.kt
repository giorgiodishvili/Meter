package com.example.meter.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App :
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