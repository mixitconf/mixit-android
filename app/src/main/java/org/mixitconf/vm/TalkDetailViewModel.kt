package org.mixitconf.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mixitconf.mixitApp
import org.mixitconf.model.entity.Talk
import org.mixitconf.model.entity.speakerIdList
import kotlin.coroutines.CoroutineContext


class TalkDetailViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    val liveData = MutableLiveData<Talk>()

    @Transaction
    fun loadSpeaker(id: String) {
        launch {
            val talk = mixitApp.talkDao.readOne(id)
            talk.speakers.addAll(mixitApp.speakerDao.readAllByIds(talk.speakerIdList))
            liveData.postValue(talk)
        }
    }
}


