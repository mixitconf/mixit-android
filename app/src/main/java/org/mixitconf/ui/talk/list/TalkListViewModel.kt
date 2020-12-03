package org.mixitconf.ui.talk.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Transaction
import kotlinx.coroutines.launch
import org.mixitconf.mixitApp
import org.mixitconf.model.entity.Talk
import org.mixitconf.model.entity.endLocale
import org.mixitconf.model.entity.startLocale


class TalkListViewModel(app: Application) : AndroidViewModel(app) {

    val liveData = MutableLiveData<List<Talk>>()

    @Transaction
    fun load() {
        viewModelScope.launch {
            val talks = mixitApp.talkDao.readAll()
            if (!talks.isNullOrEmpty()) {
                // HAck for the conf
                try {
                    liveData.postValue(talks.sortedWith(compareBy<Talk> { it.startLocale }.thenBy { it.endLocale }
                        .thenBy { it.room }))
                } catch (e: NullPointerException) {
                    liveData.postValue(talks)
                }
            }

        }
    }
}


