package com.example.meter.pushnotifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.util.Log.d
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.example.meter.MainActivity
import com.example.meter.R
import com.example.meter.repository.firebase.FirebaseMessagingRepoImpl
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.*
import javax.inject.Inject


class MyFirebaseMessagingService() :
    FirebaseMessagingService() {
    @Inject
    lateinit var firebaseMessagingRepo: FirebaseMessagingRepoImpl

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    enum class ActionType {
        COMMENT,
        LIKE
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        d("messageRecived", "${p0.data}")

        val pendingIntent: PendingIntent = if(p0.data["message"].isNullOrEmpty()){
            NavDeepLinkBuilder(application.applicationContext)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.bottom_bar_navigation)
                .setDestination(R.id.singleCommunityPostFragment)
                .setArguments(bundleOf("postId" to p0.data["postId"]))
                .createPendingIntent()
        }else{
            NavDeepLinkBuilder(application.applicationContext)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.bottom_bar_navigation)
                .setDestination(R.id.chatFragment)
                .setArguments(bundleOf("to" to p0.data["to"],"from" to p0.data["from"]))
                .createPendingIntent()
        }


        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            checkNotificationChannel("1")
        }

        val notification = NotificationCompat.Builder(applicationContext, "1")
            .setSmallIcon(R.drawable.ic_car_steering_wheel)
            .setContentTitle(p0.data["name"])
            .setContentText(p0.data["comment"])
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSound)

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification.build())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkNotificationChannel(CHANNEL_ID: String) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.app_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = "CHANNEL_DESCRIPTION"
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
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