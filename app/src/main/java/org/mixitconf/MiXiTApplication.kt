package org.mixitconf

import android.app.Application
import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import androidx.room.Room
import okhttp3.OkHttpClient
import org.mixitconf.model.dao.MiXiTDatabase
import org.mixitconf.service.initialization.DataInitializerService
import org.mixitconf.service.initialization.TalkService
import org.mixitconf.service.synchronization.SynchronizationJobService
import org.mixitconf.service.synchronization.WebsiteDao
import org.mixitconf.service.synchronization.WebsiteRestService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MiXiTApplication : Application() {

    companion object {
        const val CURRENT_EDITION = 2019
        const val SECOND:Long = 1000

        val SPECIAL_SLUG_CHARACTERS = mapOf(
            Pair('é', 'e'), Pair('è', 'e'), Pair('ï', 'i'), Pair(' ', '_'), Pair('ê', 'e'), Pair('.', '_'), Pair('\'', '_'), Pair('ô', 'o'), Pair('à', 'a'), Pair('-', '_')
        )

        val DATE_FORMAT = SimpleDateFormat("EEE", Locale.getDefault())

        const val OBJECT_ID = "id"
        const val MIXIT_API = "https://mixitconf.org/api/2019/"
        const val DATABASE_NAME = "mixitconf"
        const val FRAGMENT_ID = "fragmentId"

        const val PREF_DATA_SYNC = "sync_data"
        const val PREF_DATA_SYNC_JOB = 1
        const val PREF_FAVORITE_SYNC = "sync_favorite"
        const val PREF_SYNC_RECURRENT_UPDATE_IN_SEC = 4 * 60 *60

        fun scheduleAutomaticDataUpdate(context: Context) {
            PreferenceManager.getDefaultSharedPreferences(context).apply {
                val userWantsAutomaticUpdate = this.getBoolean(PREF_DATA_SYNC, true)
                if (userWantsAutomaticUpdate) {
                    val component = ComponentName(context, SynchronizationJobService::class.java)
                    val builder = JobInfo.Builder(PREF_DATA_SYNC_JOB, component).also {
                        it.setMinimumLatency(PREF_SYNC_RECURRENT_UPDATE_IN_SEC * SECOND)
                        // Fallback will be run in 10 seconds
                        it.setBackoffCriteria(60 * SECOND, JobInfo.BACKOFF_POLICY_LINEAR)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            it.setRequiresBatteryNotLow(true)
                            it.setRequiresStorageNotLow(true)
                        }
                        // Job will be relaunched after a reboot
                        it.setPersisted(true)
                        it.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    }
                    val jobScheduler = context.getSystemService(JobScheduler::class.java) as JobScheduler
                    jobScheduler.schedule(builder.build())
                }
            }
        }

        fun cancelDataUpdate(context: Context) {
            val jobScheduler = context.getSystemService(JobScheduler::class.java) as JobScheduler
            jobScheduler.cancel(PREF_DATA_SYNC_JOB)
        }
    }


    private val database: MiXiTDatabase by lazy {
        Room.databaseBuilder(applicationContext, MiXiTDatabase::class.java, DATABASE_NAME).build()
    }

    val talkService by lazy {
        TalkService(applicationContext)
    }
    val speakerDao by lazy {
        database.speakerDao()
    }
    val talkDao by lazy {
        database.talkDao()
    }
    val linkDao by lazy {
        database.linkDao()
    }
    val eventDao by lazy {
        database.eventDao()
    }


    val websiteDao: WebsiteDao by lazy {
        WebsiteDao(websiteRestService)
    }

    val websiteRestService: WebsiteRestService by lazy {
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.SECONDS).build()

        Retrofit.Builder().baseUrl(MIXIT_API).addConverterFactory(JacksonConverterFactory.create()).client(client).build().create(WebsiteRestService::class.java)
    }


    // Create a notification channel. Since API 26 (Android O) each app need to send notification in its own channel
    val notificationChannelId: String by lazy {
        val channelId = "mixitconf_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val priority = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, getString(R.string.channel_name), priority).apply {
                description = getString(R.string.channel_description)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        channelId
    }

    /**
     * When app is created we check that current event data are in the database
     */
    override fun onCreate() {
        super.onCreate()
        scheduleAutomaticDataUpdate(applicationContext)
        Intent(applicationContext, DataInitializerService::class.java).also { intent ->
            startService(intent)
        }
    }


    /**
     * Used to check user permission
     */
    fun hasPermission(permission: String) = ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED

}

// Extensions to expose app in object
val AndroidViewModel.mixitApp
    get() = this.getApplication<MiXiTApplication>()

val AppCompatActivity.mixitApp
    get() = this.application as MiXiTApplication

val IntentService.mixitApp
    get() = this.application as MiXiTApplication

val Fragment.mixitApp
    get() = this.activity?.application as MiXiTApplication

val JobService.mixitApp
    get() = this.application as MiXiTApplication