package org.mixitconf.service.synchronization

import android.content.Context
import androidx.room.Transaction
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import org.mixitconf.getAll
import org.mixitconf.mixitApp
import org.mixitconf.service.Workers
import org.mixitconf.service.initialization.dto.toEntity
import org.mixitconf.service.notification.Notification
import org.mixitconf.service.notification.sendEvent
import org.mixitconf.service.synchronization.dto.TalkApiDto
import org.mixitconf.service.synchronization.dto.toEntity


class TalkSynchronizationWorker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        val backgroundProcess = inputData.getBoolean(Workers.BACKGROUND_PROCESS, false)

        runCatching { mixitApp.websiteTalkService.talks().getAll() }
            .onFailure {
                Notification.ACTION_LOAD_TALKS_IN_ERROR.apply { sendEvent(context, backgroundProcess) }
            }
            .onSuccess {
                synchronizeTalks(it, backgroundProcess)
            }
        Result.success()
    }


    @Transaction
    fun synchronizeTalks(talks: List<TalkApiDto>, backgroundProcess: Boolean) {
        val nonTalkMoments = mixitApp.talkService.findNonTalkMoments()
        val ids = talks.map { it.id } + nonTalkMoments.map { it.id }

        mixitApp.talkDao.apply {
            var updated = false
            val talksToDelete = this.readAll().filter { !ids.contains(it.id) }.map { it.id }
            if (talksToDelete.isNotEmpty()) {
                this.deleteAllById(talksToDelete)
                updated = true
            }

            val talksToUpdate = talks.map { it.toEntity() } + nonTalkMoments.map { it.toEntity() }
            talksToUpdate.forEach { talk ->
                this.readOne(talk.id).also {
                    if (it == null) {
                        this.create(talk)
                        updated = true
                    } else if (it != talk) {
                        this.update(it.update(talk))
                        updated = true
                    }
                }
            }
            if (updated || !backgroundProcess) {
                Notification.ACTION_LOAD_TALKS.sendEvent(context, backProcess = backgroundProcess)
            }
        }
    }
}