package org.mixitconf.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mixitconf.mixitApp
import org.mixitconf.model.entity.Speaker
import kotlin.coroutines.CoroutineContext


class SpeakerListViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    val liveData = MutableLiveData<List<Speaker>>().also { load() }

    @Transaction
    private fun load() {
        launch {
            val speakers = mixitApp.speakerDao.readAll()
            if (!speakers.isNullOrEmpty()) {
                liveData.postValue(speakers.sortedBy { it.firstname })
            }

        }
    }
}


