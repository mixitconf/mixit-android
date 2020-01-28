package org.mixitconf.service.initialization

import android.content.Intent
import androidx.room.Transaction
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.launch
import org.mixitconf.MiXiTApplication
import org.mixitconf.R
import org.mixitconf.mixitApp
import org.mixitconf.service.MiXitService
import org.mixitconf.service.initialization.dto.EventDto
import org.mixitconf.service.initialization.dto.TalkDto
import org.mixitconf.service.initialization.dto.UserDto
import org.mixitconf.service.initialization.dto.toEntity

class DataInitializerService : MiXitService(DataInitializerService::class.simpleName) {

    /**
     * This reader is called on the first app installation to initialize database
     */
    private val events by lazy {
        val jsonInputStream = mixitApp.resources.openRawResource(R.raw.events)
        jacksonObjectMapper().readValue<List<EventDto>>(jsonInputStream)
    }

    /**
     * This reader is called on the first app installation to initialize database
     */
    private val talks by lazy {
        val jsonInputStream = mixitApp.resources.openRawResource(R.raw.talks_2019)
        val talks: List<TalkDto> = jacksonObjectMapper().readValue(jsonInputStream)
        talks + mixitApp.talkService.findNonTalkMoments()
    }

    /**
     * This reader is called on the first app installation to initialize database
     */
    private val users by lazy {
        val json = mixitApp.resources.openRawResource(R.raw.users)
        jacksonObjectMapper().readValue<List<UserDto>>(json)
    }

    override fun onHandleIntent(intent: Intent?) {
        if (mixitApp.eventDao.readOneByYear(MiXiTApplication.CURRENT_EDITION) == null) {
            launch {
                initEvents()
            }
            launch {
                initSpeakers()
            }
            launch {
                initTalks()
            }
        }
    }

    @Transaction
    fun initEvents() {
        events.filter { it.year == MiXiTApplication.CURRENT_EDITION }.forEach { mixitApp.eventDao.create(it.toEntity()) }
    }

    @Transaction
    fun initSpeakers() {
        users.forEach { user ->
            mixitApp.speakerDao.create(user.toEntity())
            user.links.forEach {
                mixitApp.linkDao.create(it.toEntity(user.login))
            }
        }
    }

    @Transaction
    fun initTalks() {
        talks.forEach { mixitApp.talkDao.create(it.toEntity()) }
    }
}