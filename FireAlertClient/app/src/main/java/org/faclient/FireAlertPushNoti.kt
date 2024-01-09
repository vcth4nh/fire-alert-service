package org.faclient

import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.PendingIntent;
import android.app.NotificationManager
import android.content.Context
import com.google.firebase.database.FirebaseDatabase

class FireAlertPushNoti : FirebaseMessagingService() {
    private val TAG = "FireAlertPushNoti"
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        showNotification(
            remoteMessage.data["title"]!!,
            remoteMessage.data["body"]!!
        )
    }

    private fun showNotification(textTitle: String, textContent: String) {
        val intent = Intent(this, EmergencyStateActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        val builder = NotificationCompat.Builder(this, getString(R.string.channel_id))
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(textTitle)
            .setContentText(textContent)
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
        val db = FirebaseDatabase.getInstance()
        db.reference
    }
}
