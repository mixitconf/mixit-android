package org.mixitconf

import android.app.Application
import android.app.IntentService
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.JobIntentService
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import androidx.room.Room
import androidx.work.CoroutineWorker
import okhttp3.OkHttpClient
import org.mixitconf.model.dao.MiXiTDatabase
import org.mixitconf.model.enums.SettingValue
import org.mixitconf.service.Workers
import org.mixitconf.service.initialization.TalkService
import org.mixitconf.service.notification.NotificationReceiver
import org.mixitconf.service.synchronization.WebsiteTalkService
import org.mixitconf.service.synchronization.WebsiteUserService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MiXiTApplication : Application() {

    companion object {
        const val CURRENT_EDITION = 2020

        val SPECIAL_SLUG_CHARACTERS = mapOf(
                Pair('é', 'e'), Pair('è', 'e'), Pair('ï', 'i'), Pair(' ', '_'), Pair('ê', 'e'), Pair('.', '_'), Pair('\'', '_'), Pair('ô', 'o'), Pair('à', 'a'), Pair('-', '_')
                                           )

        val DATE_FORMAT = SimpleDateFormat("EEE", Locale.getDefault())

        const val OBJECT_ID = "id"
        const val MIXIT_TALK_API = "https://mixitconf.org/api/2020/"
        const val MIXIT_USER_API = "https://mixitconf.org/api/external/"
        const val DATABASE_NAME = "mixitconf"
        const val FRAGMENT_ID = "fragmentId"
    }

    private val database: MiXiTDatabase by lazy {
        Room.databaseBuilder(applicationContext, MiXiTDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
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
    val eventSponsoringDao by lazy {
        database.eventSponsoringDao()
    }

    private fun createWebClient() = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(1, TimeUnit.MINUTES).writeTimeout(10, TimeUnit.SECONDS).build()

    val websiteTalkService: WebsiteTalkService by lazy {
        Retrofit.Builder().baseUrl(MIXIT_TALK_API).addConverterFactory(JacksonConverterFactory.create()).client(createWebClient()).build().create(WebsiteTalkService::class.java)
    }

    val websiteUserService: WebsiteUserService by lazy {
        Retrofit.Builder().baseUrl(MIXIT_USER_API).addConverterFactory(JacksonConverterFactory.create()).client(createWebClient()).build().create(WebsiteUserService::class.java)
    }

    /**
     * When app is created we check that current event data are in the database
     */

    override fun onCreate() {
        super.onCreate()
        registerReceiver(NotificationReceiver(), IntentFilter(NotificationReceiver.EVENT))
        Workers.createSynchronizationPeriodicWorker(this)
        Workers.createFavoritePeriodicWorker(this)
        Workers.createInitializationWorker(this)
    }

    /**
     * Used to check user permission
     */
    fun hasPermission(permission: String) = ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED

    val dataToDelete: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingValue.DATA_INITIALIZED.key, true)
}


// Extensions to expose app in object
val AndroidViewModel.mixitApp
    get() = this.getApplication<MiXiTApplication>()

val AppCompatActivity.mixitApp
    get() = this.application as MiXiTApplication

val IntentService.mixitApp
    get() = this.application as MiXiTApplication

val JobIntentService.mixitApp
    get() = this.application as MiXiTApplication

val Fragment.mixitApp
    get() = this.activity?.application as MiXiTApplication


val CoroutineWorker.mixitApp
    get() = this.applicationContext as MiXiTApplication
