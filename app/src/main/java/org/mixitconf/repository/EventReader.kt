package org.mixitconf.repository

import org.mixitconf.model.Event

/**
 * Events are read from Json file
 */
class EventReader(val events: List<Event>) {

    fun findAll(): List<Event> = events
    fun findOne(id: String): Event = events.first { it.id == id }
}