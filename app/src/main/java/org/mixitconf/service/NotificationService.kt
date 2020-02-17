package org.mixitconf.service

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Builder
import androidx.core.app.TaskStackBuilder
import org.mixitconf.MiXiTApplication
import org.mixitconf.R
import org.mixitconf.mixitApp
import org.mixitconf.view.ui.MainActivity


enum class Notification {
    ACTION_LOAD_SPEAKERS, ACTION_LOAD_TALKS, ACTION_LOAD_SPEAKERS_IN_ERROR, ACTION_LOAD_TALKS_IN_ERROR
}

const val MESSAGE_KEY = " org.mixitconf.service.NotificationMessage"

class NotificationService : IntentService(NotificationService::class.java.simpleName) {

    private val NOTIFICATION_ID = 1

    companion object {

        fun startNotification(context: Context, notification: Notification, message: String? = null) = context.startService(Intent(context, NotificationService::class.java).apply {
            action = notification.name
            putExtra(MESSAGE_KEY, message ?: "")
        })
    }

    override fun onHandleIntent(intent: Intent?) {
        val (message, fragmentId) = when (intent!!.action) {
            Notification.ACTION_LOAD_SPEAKERS.name -> Pair(R.string.sync_speaker, R.id.navigation_speaker)
            Notification.ACTION_LOAD_TALKS.name -> Pair(R.string.sync_talk, R.id.navigation_talk)
            Notification.ACTION_LOAD_SPEAKERS_IN_ERROR.name -> Pair(R.string.sync_speaker_error, null)
            Notification.ACTION_LOAD_TALKS_IN_ERROR.name -> Pair(R.string.sync_talk_error, null)
            else -> throw IllegalArgumentException()
        }


        val notifBuilder = Builder(this, mixitApp.notificationChannelId).setSmallIcon(R.drawable.mxt_logo_x_dark).setContentTitle(this.getString(R.string.app_name)).setContentText(this.getString(message) + intent.getStringExtra(MESSAGE_KEY)).setPriority(NotificationCompat.PRIORITY_HIGH)


        var notif: android.app.Notification

        // Create an Intent for the activity to start
        if (fragmentId == null) {
            notif = notifBuilder.build()
        } else {
            val resultIntent = Intent(this, MainActivity::class.java)
            resultIntent.putExtra(MiXiTApplication.FRAGMENT_ID, fragmentId)

            // Create the TaskStackBuilder
            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
                // Add the intent, which inflates the back stack
                addNextIntentWithParentStack(resultIntent)
                // Get the PendingIntent containing the entire back stack
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            notif = notifBuilder.setContentIntent(resultPendingIntent).build()

        }

        val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notif)
    }
}