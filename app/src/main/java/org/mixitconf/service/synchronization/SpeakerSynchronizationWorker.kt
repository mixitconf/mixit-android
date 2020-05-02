package org.mixitconf.service.synchronization

import android.content.Context
import androidx.room.Transaction
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import org.mixitconf.getAll
import org.mixitconf.mixitApp
import org.mixitconf.service.Workers
import org.mixitconf.service.notification.Notification
import org.mixitconf.service.notification.sendEvent
import org.mixitconf.service.synchronization.dto.SpeakerApiDto
import org.mixitconf.service.synchronization.dto.toEntity


class SpeakerSynchronizationWorker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {


    override suspend fun doWork(): Result = coroutineScope {
        val backgroundProcess = inputData.getBoolean(Workers.BACKGROUND_PROCESS, false)

        runCatching { mixitApp.websiteTalkService.speakers().getAll() }
            .onFailure {
                Notification.ACTION_LOAD_SPEAKERS_IN_ERROR.apply { sendEvent(context, backgroundProcess) }
            }
            .onSuccess {
                synchronizeSpeakers(it, backgroundProcess)
            }
        Result.success()
    }


    @Transaction
    fun synchronizeSpeakers(speakers: List<SpeakerApiDto>, backgroundProcess: Boolean) {
        val logins = speakers.map { it.login }

        mixitApp.speakerDao.apply {
            var updated = false
            val speakersToDelete = this.readAll().filterNot { logins.contains(it.login) }.map { it.login }
            if (speakersToDelete.isNotEmpty()) {
                this.deleteAllById(speakersToDelete)
                updated = true
            }

            speakers.forEach { speaker ->
                this.readOne(speaker.login!!).also {
                    if (it == null) {
                        this.create(speaker.toEntity())
                        updated = true
                    } else if (it != speaker.toEntity()) {
                        this.update(speaker.toEntity())
                        updated = true
                    }
                }
            }
            if (updated || !backgroundProcess) {
                Notification.ACTION_LOAD_SPEAKERS.sendEvent(context, backProcess = backgroundProcess)
            }
        }
    }
}