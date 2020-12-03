package org.mixitconf.ui.speaker.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Transaction
import kotlinx.coroutines.launch
import org.mixitconf.mixitApp
import org.mixitconf.model.entity.Speaker


class SpeakerListViewModel(app: Application) : AndroidViewModel(app) {

    val liveData = MutableLiveData<List<Speaker>>().also { load() }

    @Transaction
    private fun load() {
        viewModelScope.launch {
            val speakers = mixitApp.speakerDao.readAll()
            if (!speakers.isNullOrEmpty()) {
                liveData.postValue(speakers.sortedBy { it.firstname })
            }

        }
    }
}


