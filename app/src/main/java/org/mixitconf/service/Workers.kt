package org.mixitconf.service

import android.content.Context
import androidx.work.*
import org.mixitconf.booleanSharedPrefs
import org.mixitconf.model.enums.SettingValue
import org.mixitconf.service.favorite.FavoritePeriodicWorker
import org.mixitconf.service.initialization.InitializationWorker
import org.mixitconf.service.synchronization.SpeakerSynchronizationWorker
import org.mixitconf.service.synchronization.SynchronizationPeriodicWorker
import org.mixitconf.service.synchronization.TalkSynchronizationWorker
import java.util.concurrent.TimeUnit

class Workers {

    companion object {
        val BACKGROUND_PROCESS = "BACKGROUND_PROCESS"
        val TAG_SYNC = "SYNC"
        val TAG_FAVORITE = "FAVORITE"

        val commonWorkerConstraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        fun createFavoritePeriodicWorker(context: Context) {
            if (context.booleanSharedPrefs(SettingValue.FAVORITE_NOTIFICATION_ENABLE)) {
                val work = PeriodicWorkRequestBuilder<FavoritePeriodicWorker>(1, TimeUnit.MINUTES)
                    .addTag(TAG_FAVORITE)
                    .build()
                WorkManager.getInstance(context).enqueue(work)
            }
        }

        fun cancelFavoritePeriodicWorker(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(TAG_FAVORITE)
        }

        fun createSynchronizationPeriodicWorker(context: Context) {
            if (context.booleanSharedPrefs(SettingValue.DATA_AUTO_SYNC_ENABLE)) {
                val work = PeriodicWorkRequestBuilder<SynchronizationPeriodicWorker>(4, TimeUnit.HOURS)
                    .addTag(TAG_SYNC)
                    .setConstraints(commonWorkerConstraints)
                    .build()
                WorkManager.getInstance(context).enqueue(work)
            }
        }

        fun cancelSynchronizationPeriodicWorker(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(TAG_SYNC)
        }

        fun createTalkSynchronizationWorker(context: Context, backgroundProcess: Boolean = false) {
            val work = OneTimeWorkRequestBuilder<TalkSynchronizationWorker>()
                .setInputData(workDataOf(BACKGROUND_PROCESS to backgroundProcess))
                .setConstraints(commonWorkerConstraints)
                .build()
            WorkManager.getInstance(context).enqueue(work)
        }

        fun createSpeakerSynchronizationWorker(context: Context, backgroundProcess: Boolean = false) {
            val work = OneTimeWorkRequestBuilder<SpeakerSynchronizationWorker>()
                .setInputData(workDataOf(BACKGROUND_PROCESS to backgroundProcess))
                .setConstraints(commonWorkerConstraints)
                .build()
            WorkManager.getInstance(context).enqueue(work)
        }

        fun createInitializationWorker(context: Context) {
            val work = OneTimeWorkRequestBuilder<InitializationWorker>().build()
            WorkManager.getInstance(context).enqueue(work)
        }


    }
}