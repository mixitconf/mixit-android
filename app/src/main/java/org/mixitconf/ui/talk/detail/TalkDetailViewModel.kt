package org.mixitconf.ui.talk.detail

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
        get() = Dispatchers.IO

    val liveData = MutableLiveData<Talk>()

    @Transaction
    fun loadTalk(id: String) {
        launch {
            val talk = mixitApp.talkDao.readOne(id)!!
            talk.speakers.addAll(mixitApp.speakerDao.readAllByIds(talk.speakerIdList))
            liveData.postValue(talk)
        }
    }

    @Transaction
    fun saveTalk(talk: Talk) {
        launch {
            mixitApp.talkDao.update(talk)
            loadTalk(talk.id)
        }
    }
}


