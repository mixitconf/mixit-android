package org.mixitconf.service.initialization

import androidx.room.Transaction
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.coroutineScope
import org.mixitconf.MiXiTApplication
import org.mixitconf.R
import org.mixitconf.service.initialization.dto.EventDto
import org.mixitconf.service.initialization.dto.TalkDto
import org.mixitconf.service.initialization.dto.UserDto
import org.mixitconf.service.initialization.dto.toEntity


class InitializationWorker(val mixitApp: MiXiTApplication, params: WorkerParameters) : CoroutineWorker(mixitApp, params) {

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
        val jsonInputStream = mixitApp.resources.openRawResource(R.raw.talk)
        val talks: List<TalkDto> = jacksonObjectMapper().readValue(jsonInputStream)
        talks + mixitApp.talkService.findNonTalkMoments()
    }

    /**
     * This reader is called on the first app installation to initialize database
     */
    private val users by lazy {
        val json = mixitApp.resources.openRawResource(R.raw.speakers)
        jacksonObjectMapper().readValue<List<UserDto>>(json)
    }


    override suspend fun doWork(): Result = coroutineScope {
        init()
        Result.success()
    }


    @Transaction
    fun init() {
        initEvents()
        initSpeakers()
        initTalks()
    }

    fun initEvents() {
        events.filter { it.year == MiXiTApplication.CURRENT_EDITION }.forEach { mixitApp.eventDao.create(it.toEntity()) }
    }

    fun initSpeakers() {
        users.forEach { user ->
            if (mixitApp.speakerDao.readOne(user.login) != null) {
                mixitApp.speakerDao.update(user.toEntity())
            } else {
                mixitApp.speakerDao.create(user.toEntity())
            }
            user.links.forEach {
                mixitApp.linkDao.create(it.toEntity(user.login))
            }
        }
    }

    fun initTalks() {
        talks.forEach {
            if (mixitApp.talkDao.readOne(it.id) != null) {
                mixitApp.talkDao.update(it.toEntity())
            } else {
                mixitApp.talkDao.create(it.toEntity())
            }
        }
    }
}