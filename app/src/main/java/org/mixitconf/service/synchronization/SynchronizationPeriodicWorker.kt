package org.mixitconf.service.synchronization

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import org.mixitconf.service.Workers


class SynchronizationPeriodicWorker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        Workers.createTalkSynchronizationWorker(context)
        Workers.createSpeakerSynchronizationWorker(context)
        Result.success()
    }
}
