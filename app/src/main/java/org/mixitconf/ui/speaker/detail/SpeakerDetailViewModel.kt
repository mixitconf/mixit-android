package org.mixitconf.ui.speaker.detail

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


class SpeakerDetailViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    val liveData = MutableLiveData<Speaker>()

    @Transaction
    fun loadSpeaker(id: String) {
        launch {
            val speaker = mixitApp.speakerDao.readOne(id)!!
            speaker.talks.clear()
            speaker.talks.addAll(mixitApp.talkDao.readAllBySpeakerId(speaker.login))
            speaker.links.clear()
            speaker.links.addAll(mixitApp.linkDao.readAllBySpeakerId(speaker.login))
            liveData.postValue(speaker)
        }
    }

}


