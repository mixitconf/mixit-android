package org.mixitconf.service.synchronization

import android.content.Intent
import android.widget.Toast
import androidx.room.Transaction
import kotlinx.coroutines.launch
import org.mixitconf.R
import org.mixitconf.mixitApp
import org.mixitconf.service.MiXitService
import org.mixitconf.service.Notification
import org.mixitconf.service.NotificationService
import org.mixitconf.service.initialization.dto.toEntity
import org.mixitconf.service.synchronization.dto.toEntity


class SynchronizationService : MiXitService(SynchronizationService::class.simpleName) {

    companion object{
        const val AUTOMATIC_TASK = "org.mixitconf.service.synchronization.auto"
    }

    override fun onHandleIntent(intent: Intent?) {
        val backProcess = intent?.getBooleanExtra(AUTOMATIC_TASK, false) ?: false

        launch {
            synchronizeSpeakers(backProcess)
        }
        launch {
            // TODO
            synchronizeTalks(false)
        }
    }

    @Transaction
    fun synchronizeSpeakers(backProcess: Boolean) {
        callApi(mixitApp.websiteTalkService.speakers(), Notification.ACTION_LOAD_SPEAKERS_IN_ERROR, backProcess) { users ->
            val logins = users.map { it.login }

            mixitApp.speakerDao.apply {
                var update = false
                val speakersToDelete = this.readAll().filterNot { logins.contains(it.login) }.map { it.login }
                if (speakersToDelete.isNotEmpty()) {
                    this.deleteAllById(speakersToDelete)
                    update = true
                }

                users.forEach {
                    val login = it.login!!
                    val existingUser = this.readOne(login)
                    if (existingUser != null) {
                        if (existingUser != it.toEntity()) {
                            this.update(it.toEntity())
                            update = true
                        }
                    } else {
                        this.create(it.toEntity())
                        update = true
                    }
                    mixitApp.linkDao.deleteBySpeaker(login)
                    it.links.forEach { link ->
                        mixitApp.linkDao.create(link.toEntity(login))
                    }
                }
                if(!backProcess){
                    Toast.makeText(mixitApp, R.string.sync_finish, Toast.LENGTH_LONG).show()
                }
                else if (update) {
                    NotificationService.startNotification(mixitApp, Notification.ACTION_LOAD_SPEAKERS)
                }
            }
        }
    }

    @Transaction
    fun synchronizeTalks(backProcess: Boolean) {
        callApi(mixitApp.websiteTalkService.talks(), Notification.ACTION_LOAD_TALKS_IN_ERROR, backProcess) { talks ->
            val nonTalkMoments = mixitApp.talkService.findNonTalkMoments()
            val ids = talks.map { it.id } + nonTalkMoments.map { it.id }

            mixitApp.talkDao.apply {
                var update = false
                val talksToDelete = this.readAll().filter { !ids.contains(it.id) }.map { it.id }
                if (talksToDelete.isNotEmpty()) {
                    this.deleteAllById(talksToDelete)
                    update = true
                }

                val talksToUpdate = talks.map { it.toEntity() } + nonTalkMoments.map { it.toEntity() }
                talksToUpdate.forEach {
                    val existingTalk = this.readOne(it.id)
                    if (existingTalk != null) {
                        if (existingTalk != it) {
                            this.update(it)
                            update = true
                        }
                    } else {
                        this.create(it)
                        update = true
                    }
                }
                if (update) {
                    NotificationService.startNotification(mixitApp, Notification.ACTION_LOAD_TALKS)
                }
            }
        }
    }

}
