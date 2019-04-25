package org.mixitconf.service.synchronization

import android.content.Intent
import androidx.room.Transaction
import kotlinx.coroutines.launch
import org.mixitconf.R
import org.mixitconf.mixitApp
import org.mixitconf.service.MiXitService
import org.mixitconf.service.synchronization.dto.TalkApiDto
import org.mixitconf.service.synchronization.dto.UserApiDto
import org.mixitconf.service.synchronization.dto.toEntity
import retrofit2.Call
import retrofit2.http.GET

interface MiXiTApiCaller {

    @GET("speaker")
    fun speakers(): Call<List<UserApiDto>>

    @GET("talk")
    fun talks(): Call<List<TalkApiDto>>

}

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
        callApi(mixitApp.miXiTApiCaller.speakers(), R.string.error_sync_speakers) { users ->
            val logins = users.map { it.login }

            mixitApp.speakerDao.apply {
                val speakersToDelete = this.readAll().filter { !logins.contains(it.login) }.map { it.login }
                if (speakersToDelete.isNotEmpty()) {
                    this.deleteAllById(speakersToDelete)
                }

                users.forEach {
                    val login = it.login!!
                    if(this.readOne(login) != null) this.update(it.toEntity()) else this.create(it.toEntity())
                    mixitApp.linkDao.deleteBySpeaker(login)
                    it.links.forEach {
                        mixitApp.linkDao.create(it.toEntity(login))
                    }
                }
                toast(mixitApp.getText(R.string.sync_data))
            }
        }
    }

    @Transaction
    fun synchronizeTalks() {
        callApi(mixitApp.miXiTApiCaller.talks(), R.string.error_sync_talks) { talks ->
            val ids = talks.map { it.id } + mixitApp.talkService.findNonTalkMoments().map { it.id }

            mixitApp.talkDao.apply {
                val talksToDelete = this.readAll().filter { !ids.contains(it.id) }.map { it.id }
                if (talksToDelete.isNotEmpty()) {
                    this.deleteAllById(talksToDelete)
                }
                talks.forEach {
                    if(this.readOne(it.id!!) != null) this.update(it.toEntity()) else this.create(it.toEntity())
                }
                toast(mixitApp.getText(R.string.sync_data))
            }
        }
    }
}