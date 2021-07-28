package com.example.meter.pushnotifications

import android.content.Context
import android.util.Log
import android.util.Log.d
import com.example.meter.repository.firebase.FirebaseMessagingRepoImpl
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.*
import javax.inject.Inject

class MyFirebaseMessagingService @Inject constructor(private val firebaseMessagingRepo: FirebaseMessagingRepoImpl) :
    FirebaseMessagingService() {


    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    enum class ActionType {
        COMMENT,
        LIKE
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        d("messageRecived", "${p0.data}")
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.e("newToken", p0)
        val oldToken = getSharedPreferences("_", MODE_PRIVATE).getString("token", "")!!
        if (oldToken.isNotEmpty()) {
            scope.launch {
                withContext(Dispatchers.IO) {
                    firebaseMessagingRepo.updateOldToken(
                        p0,
                        oldToken
                    )
                }
            }

        }

        getSharedPreferences("_", MODE_PRIVATE).edit().putString("token", p0).apply()
    }

    companion object {
        fun getToken(context: Context): String? =
            context.getSharedPreferences("_", MODE_PRIVATE).getString("token", "empty")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()

    }
}