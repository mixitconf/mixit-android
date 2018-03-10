package org.mixitconf.repository

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.mixitconf.R
import org.mixitconf.SingletonHolder
import org.mixitconf.model.Event

/**
 * Events are read from Json file
 */
class EventReader(private val context: Context) {

    val objectMapper: ObjectMapper = jacksonObjectMapper()

    private fun readFile(): List<Event>{
        val jsonInputStream = context.resources.openRawResource(R.raw.events)
        val events: List<Event> = objectMapper.readValue(jsonInputStream)
        return events
    }

    fun findAll(): List<Event> = readFile()

    fun findOne(id: String): Event = readFile().filter { it.id == id }.first()

    companion object : SingletonHolder<EventReader, Context>(::EventReader)
}