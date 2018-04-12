package org.mixitconf.repository

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.mixitconf.R
import org.mixitconf.model.Event
import org.mixitconf.service.SingletonHolder

/**
 * Events are read from Json file
 */
class EventReader(val events: List<Event>) {

    fun findAll(): List<Event> = events
    fun findOne(id: String): Event = events.first { it.id == id }

    companion object : SingletonHolder<EventReader, Context>({
            val jsonInputStream = it.resources.openRawResource(R.raw.events)
            val events:List<Event> =  jacksonObjectMapper().readValue(jsonInputStream)
            EventReader(events)
    })
}