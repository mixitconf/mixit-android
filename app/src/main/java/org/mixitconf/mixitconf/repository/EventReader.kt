package org.mixitconf.mixitconf.repository

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.mixitconf.mixitconf.R
import org.mixitconf.mixitconf.model.Event

/**
 * Events are read from Json file
 */
class EventReader {

    val objectMapper: ObjectMapper = jacksonObjectMapper()

    private fun readFile(context: Context): List<Event>{
        val jsonInputStream = context.resources.openRawResource(R.raw.events)
        val events: List<Event> = objectMapper.readValue(jsonInputStream)
        return events
    }

    fun findAll(context: Context): List<Event> = readFile(context)

    fun findOne(context: Context, id: String): Event = readFile(context).filter { it.id == id }.first()
}