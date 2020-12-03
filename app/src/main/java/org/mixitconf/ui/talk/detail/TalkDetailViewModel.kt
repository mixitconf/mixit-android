package org.mixitconf.ui.talk.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Transaction
import kotlinx.coroutines.launch
import org.mixitconf.mixitApp
import org.mixitconf.model.entity.Talk
import org.mixitconf.model.entity.speakerIdList


class TalkDetailViewModel(app: Application) : AndroidViewModel(app) {

    val liveData = MutableLiveData<Talk>()

    @Transaction
    fun loadTalk(id: String) {
        viewModelScope.launch {
            val talk = mixitApp.talkDao.readOne(id)!!
            talk.speakers.addAll(mixitApp.speakerDao.readAllByIds(talk.speakerIdList))
            liveData.postValue(talk)
        }
    }

    @Transaction
    fun saveTalk(talk: Talk) {
        viewModelScope.launch {
            mixitApp.talkDao.update(talk)
            loadTalk(talk.id)
        }
    }
}


