package org.mixitconf.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Builder
import androidx.core.app.TaskStackBuilder
import org.mixitconf.MiXiTApplication
import org.mixitconf.R
import org.mixitconf.ui.MainActivity


enum class Notification(val messageId: Int, val fragmentId: Int?, val notificationId: Int) {
    ACTION_LOAD_SPEAKERS(R.string.sync_speaker, R.id.navigation_speaker, 1),
    ACTION_LOAD_TALKS(R.string.sync_talk, R.id.navigation_talk, 2),
    ACTION_LOAD_SPEAKERS_IN_ERROR(R.string.sync_speaker_error, null, 3),
    ACTION_LOAD_TALKS_IN_ERROR(R.string.sync_talk_error, null, 4),
    FAVORITES(R.string.favorite_will_start, null, 99)
}

fun Notification.sendEvent(context: Context, backProcess: Boolean = true, message: String? = null, id: Int? = null) = context.sendBroadcast(Notification.ACTION_LOAD_TALKS.let {
    Intent(NotificationReceiver.EVENT).apply {
        putExtra(NotificationReceiver.MESSAGE, message ?: context.getString(it.messageId))
        putExtra(NotificationReceiver.ID, id ?: it.notificationId)
        putExtra(NotificationReceiver.BACKROUND, backProcess)
        if (it.fragmentId != null) {
            putExtra(NotificationReceiver.FRAGMENT_ID, it.fragmentId)
        }
    }
})

/**
 * This receiver manage all notification request (from a background thread or not)
 */
class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val EVENT = " org.mixitconf.service.NotificationEvent"
        const val MESSAGE = " org.mixitconf.service.NotificationMessage"
        const val ID = " org.mixitconf.service.NotificationId"
        const val BACKROUND = " org.mixitconf.service.NotificationBackground"
        const val FRAGMENT_ID = " org.mixitconf.service.NotificationFragmentId"

    }


    override fun onReceive(context: Context, intent: Intent?) {
        intent?.action?.let {
            val message = intent.getStringExtra(MESSAGE)
            val fragmentId = intent.getIntExtra(FRAGMENT_ID, -1)
            val id = intent.getIntExtra(ID, 1)
            val backProcess = intent.getBooleanExtra(BACKROUND, false)

            if (backProcess) {
                val notifBuilder = Builder(context, notificationChannelId(context))
                    .setSmallIcon(R.drawable.mxt_logo_x_dark)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(message).setPriority(NotificationCompat.PRIORITY_HIGH)

                var notif: android.app.Notification

                // Create an Intent for the activity to start
                if (fragmentId <= 0) {
                    notif = notifBuilder.build()
                } else {
                    val resultIntent = Intent(context, MainActivity::class.java)
                    resultIntent.putExtra(MiXiTApplication.FRAGMENT_ID, fragmentId)

                    // Create the TaskStackBuilder
                    val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                        // Add the intent, which inflates the back stack
                        addNextIntentWithParentStack(resultIntent)
                        // Get the PendingIntent containing the entire back stack
                        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                    }

                    notif = notifBuilder.setContentIntent(resultPendingIntent).build()

                }

                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(id, notif)
            } else {
                Toast.makeText(context, R.string.sync_finish, Toast.LENGTH_LONG).show()
            }

        }
    }

    // Create a notification channel. Since API 26 (Android O) each app need to send notification in its own channel
    fun notificationChannelId(context: Context): String {
        val channelId = "mixit_conf_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val priority = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, context.getString(R.string.channel_name), priority).apply {
                description = context.getString(R.string.channel_description)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        return channelId
    }


}