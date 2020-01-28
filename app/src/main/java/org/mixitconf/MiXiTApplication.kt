package org.mixitconf

import android.app.Application
import android.app.IntentService
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import okhttp3.OkHttpClient
import org.mixitconf.model.dao.MiXiTDatabase
import org.mixitconf.service.initialization.DataInitializerService
import org.mixitconf.service.initialization.TalkService
import org.mixitconf.service.synchronization.MiXiTApiCaller
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MiXiTApplication : Application() {

    companion object {
        const val CURRENT_EDITION = 2019
        val SPECIAL_SLUG_CHARACTERS = mapOf(
            Pair('é', 'e'), Pair('è', 'e'), Pair('ï', 'i'), Pair(' ', '_'), Pair('ê', 'e'), Pair('.', '_'), Pair('\'', '_'), Pair('ô', 'o'), Pair('à', 'a'), Pair('-', '_')
        )

        val DATE_FORMAT = SimpleDateFormat("EEE", Locale.getDefault())
        const val OBJECT_ID = "id"
        const val MIXIT_API = "https://mixitconf.org/api/2019/"
        const val DATABASE_NAME = "mixitconf"
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

    val miXiTApiCaller: MiXiTApiCaller by lazy {
        val client = OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(10, TimeUnit.SECONDS).build()

        Retrofit.Builder().baseUrl(MIXIT_API).addConverterFactory(JacksonConverterFactory.create()).client(client).build().create(MiXiTApiCaller::class.java)
    }

    /**
     * When app is created we check that current event data are in the database
     */
    override fun onCreate() {
        super.onCreate()
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