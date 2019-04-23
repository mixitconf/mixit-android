package org.mixitconf.repository.readers

import org.mixitconf.model.dto.EventDto

/**
 * Events are read from Json file
 */
class EventReader(private val events: List<EventDto>) {

    fun findAll(): List<EventDto> = events
    fun findOne(id: String): EventDto = events.first { it.id == id }
}