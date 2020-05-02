package org.mixitconf.ui.talk.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mixitconf.mixitApp
import org.mixitconf.model.entity.Talk
import org.mixitconf.model.entity.endLocale
import org.mixitconf.model.entity.startLocale
import kotlin.coroutines.CoroutineContext


class TalkListViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    val liveData = MutableLiveData<List<Talk>>()

    @Transaction
    fun load() {
        launch {
            val talks = mixitApp.talkDao.readAll()
            if (!talks.isNullOrEmpty()) {
                // HAck for the conf
                try {
                    liveData.postValue(talks.sortedWith(compareBy<Talk> { it.startLocale }.thenBy { it.endLocale }.thenBy { it.room }))
                } catch (e: NullPointerException) {
                    liveData.postValue(talks)
                }
            }

        }
    }
}


