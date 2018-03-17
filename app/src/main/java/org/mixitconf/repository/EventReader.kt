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
class EventReader(private val context: Context) {

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    private fun readFile(): List<Event>{
        val jsonInputStream = context.resources.openRawResource(R.raw.events)
        val events: List<Event> = objectMapper.readValue(jsonInputStream)
        return events
    }

    fun findAll(): List<Event> = readFile()

    fun findOne(id: String): Event = readFile().first { it.id == id }

    companion object : SingletonHolder<EventReader, Context>(::EventReader)
}