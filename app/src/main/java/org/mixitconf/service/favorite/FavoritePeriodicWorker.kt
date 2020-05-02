package org.mixitconf.service.favorite

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import org.mixitconf.durationInText
import org.mixitconf.longSharedPrefs
import org.mixitconf.mixitApp
import org.mixitconf.model.enums.SettingValue
import org.mixitconf.model.enums.isTalk
import org.mixitconf.service.notification.Notification.FAVORITES
import org.mixitconf.service.notification.sendEvent


class FavoritePeriodicWorker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        // We have to know how many talk will start in the next minutes
        mixitApp.talkDao
            .readAll()
            .filter { it.favorite && it.format.isTalk() }
            .filter {
                it.startSoon(context.longSharedPrefs(SettingValue.FAVORITE_NOTIFICATION_DURATION, 5))
            }
            .forEach {
                FAVORITES.sendEvent(context,
                                    true,
                                    String.format(context.getString(FAVORITES.messageId),
                                                  it.title,
                                                  it.start.durationInText,
                                                  context.getString(it.room.i18nId)),
                                    it.hashCode())
            }
        Result.success()
    }
}

