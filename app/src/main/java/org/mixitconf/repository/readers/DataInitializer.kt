package org.mixitconf.repository.readers

import android.content.Context
import androidx.room.Transaction
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mixitconf.R
import org.mixitconf.model.dto.EventDto
import org.mixitconf.model.dto.TalkDto
import org.mixitconf.model.dto.UserDto
import org.mixitconf.model.dto.toEntity
import org.mixitconf.repository.dao.MiXiTDatabase
import org.mixitconf.service.Constant
import kotlin.coroutines.CoroutineContext

class DataInitializer(val context: Context, val database: MiXiTDatabase) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    /**
     * This reader is called on the first app installation to initialize database
     */
    val eventReader by lazy {
        val jsonInputStream = context.resources.openRawResource(R.raw.events)
        val events: List<EventDto> = jacksonObjectMapper().readValue(jsonInputStream)
        EventReader(events)
    }

    /**
     * This reader is called on the first app installation to initialize database
     */
    val talkReader by lazy {
        val jsonInputStream = context.resources.openRawResource(R.raw.talks_2019)
        val talks: List<TalkDto> = jacksonObjectMapper().readValue(jsonInputStream)
        TalkReader(talks, context)
    }

    /**
     * This reader is called on the first app installation to initialize database
     */
    val userReader by lazy {
        val json = context.resources.openRawResource(R.raw.users)
        val users: List<UserDto> = jacksonObjectMapper().readValue(json)
        UserReader(users)
    }

    @Transaction
    fun initialize() {
        launch {
            if (database.eventDao().readOneByYear(Constant.CURRENT_EDITION) == null) {
                // Events
                eventReader
                    .findAll()
                    .filter { it.year == Constant.CURRENT_EDITION }
                    .forEach { event ->
                        database.eventDao().create(event.toEntity())
                        event.sponsors.forEach {
                            database.eventSponsoringDao().create(it.toEntity(event.id))
                        }
                    }
                // Talks
                talkReader.findAll().forEach {
                    database.talkDao().create(it.toEntity())
                }
                // Speakers
                userReader.findAll().forEach { user ->
                    database.speakerDao().create(user.toEntity())
                    user.links.forEach {
                        database.linkDao().create(it.toEntity(user.login))
                    }
                }
            }
        }
    }

}