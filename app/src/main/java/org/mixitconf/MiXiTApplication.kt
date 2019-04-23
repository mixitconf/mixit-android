package org.mixitconf

import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.mixitconf.repository.dao.MiXiTDatabase
import org.mixitconf.repository.readers.DataInitializer
import kotlin.coroutines.CoroutineContext


class MiXiTApplication : Application(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    val DATABASE_NAME = "mixitconf"

    /**
     * Used to check user permission
     */
    fun hasPermission(permission: String) = ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED

    private val database: MiXiTDatabase by lazy {
        Room.databaseBuilder(applicationContext, MiXiTDatabase::class.java, DATABASE_NAME)
            .build()
    }

    val speakerDao
        get() = database.speakerDao()

    val talkDao
        get() = database.talkDao()

    val eventDao
        get() = database.eventDao()

    val dataInitializer by lazy {
        DataInitializer(applicationContext, database)
    }

    /**
     * When app is created we check that current event data are in the database
     */
    override fun onCreate() {
        super.onCreate()
        // TODO to delete
        deleteDatabase(DATABASE_NAME)
        dataInitializer.initialize()
    }
}

// Extensions to expose app in object

val AndroidViewModel.mixitApp
    get() = this.getApplication<MiXiTApplication>()

val Fragment.mixitApp
    get() = this.activity?.application as MiXiTApplication