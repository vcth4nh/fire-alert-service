package org.faclient

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.PendingIntent;
import android.app.NotificationManager
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FireAlertPushNoti : FirebaseMessagingService() {
    private val TAG = "FireAlertPushNoti"
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        showNotification(
            remoteMessage.data["title"]!!,
            remoteMessage.data["location"]!!,
            remoteMessage.data["time"]!!.toLong()
        )
    }

    private fun showNotification(title: String, location: String, time: Long) {
//        TODO: add head-up notification when the phone is unlocked

        setEmergencyOn(location, time)
        val intent = Intent(this, EmergencyStateActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, getString(R.string.channel_id))
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(title)
            .setContentText(location)
            .setPriority(NotificationManager.IMPORTANCE_MAX)
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(pendingIntent, true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1312, builder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")

    }

    private fun regTokenWithUser(token: String) {
        // TODO: push device token to firebase database
        val db = FirebaseDatabase.getInstance()
        db.reference
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setEmergencyOn(location: String, time: Long) {
        val settingStorage = SettingStorage(application)
        GlobalScope.launch {
            settingStorage.startEmergency()
            settingStorage.setLocation(location)
            settingStorage.setTime(time)
        }
    }
}
