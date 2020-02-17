package org.mixitconf.service.synchronization

import android.content.Intent
import androidx.room.Transaction
import kotlinx.coroutines.launch
import org.mixitconf.service.NotificationService
import org.mixitconf.R
import org.mixitconf.mixitApp
import org.mixitconf.service.MiXitService
import org.mixitconf.service.Notification
import org.mixitconf.service.initialization.dto.toEntity
import org.mixitconf.service.synchronization.dto.toEntity


class SynchronizationService : MiXitService(SynchronizationService::class.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        launch {
            synchronizeSpeakers()
        }
        launch {
            synchronizeTalks()
        }
    }

    @Transaction
    fun synchronizeSpeakers() {
        callApi(mixitApp.websiteRestService.speakers(), Notification.ACTION_LOAD_SPEAKERS_IN_ERROR) { users ->
            val logins = users.map { it.login }

            mixitApp.speakerDao.apply {
                val speakersToDelete = this.readAll().filter { !logins.contains(it.login) }.map { it.login }
                if (speakersToDelete.isNotEmpty()) {
                    this.deleteAllById(speakersToDelete)
                }

                users.forEach {
                    val login = it.login!!
                    if (this.readOne(login) != null) this.update(it.toEntity()) else this.create(it.toEntity())
                    mixitApp.linkDao.deleteBySpeaker(login)
                    it.links.forEach { link ->
                        mixitApp.linkDao.create(link.toEntity(login))
                    }
                }
                NotificationService.startNotification(mixitApp, Notification.ACTION_LOAD_SPEAKERS)
            }
        }
    }

    @Transaction
    fun synchronizeTalks() {
        callApi(mixitApp.websiteRestService.talks(), Notification.ACTION_LOAD_TALKS_IN_ERROR) { talks ->
            val nonTalkMoments = mixitApp.talkService.findNonTalkMoments()
            val ids = talks.map { it.id } + nonTalkMoments.map { it.id }

            mixitApp.talkDao.apply {
                val talksToDelete = this.readAll().filter { !ids.contains(it.id) }.map { it.id }
                if (talksToDelete.isNotEmpty()) {
                    this.deleteAllById(talksToDelete)
                }

                val talksToUpdate = talks.map { it.toEntity() } + nonTalkMoments.map { it.toEntity() }
                talksToUpdate.forEach {
                    if (this.readOne(it.id) != null) this.update(it) else this.create(it)
                }
                NotificationService.startNotification(mixitApp, Notification.ACTION_LOAD_TALKS)
            }
        }
    }


}
