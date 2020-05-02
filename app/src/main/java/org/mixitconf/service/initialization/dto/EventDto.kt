package org.mixitconf.service.initialization.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.mixitconf.model.entity.Event
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class EventDto(val id: String, val start: Date, val end: Date, val current: Boolean = false, val sponsors: List<EventSponsoringDto> = emptyList(), val photoUrls: List<LinkDto> = emptyList(), val year: Int)

fun EventDto.toEntity() = Event(id, start, end, year)
