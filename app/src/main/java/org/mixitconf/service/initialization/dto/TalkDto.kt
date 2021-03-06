package org.mixitconf.service.initialization.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.mixitconf.model.entity.Talk
import org.mixitconf.model.enums.Language
import org.mixitconf.model.enums.Room
import org.mixitconf.model.enums.TalkFormat
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class TalkDto(val format: TalkFormat,
                   val event: String,
                   val title: String,
                   val summary: String,
                   val speakerIds: List<String>,
                   val language: Language = Language.FRENCH,
                   val addedAt: Date,
                   val description: String?,
                   val topic: String,
                   val room: Room,
                   val start: Date,
                   val end: Date,
                   val id: String)

fun TalkDto.toEntity() = Talk(id, format, event, title, summary, speakerIds.joinToString(","), language, description, topic, room, start, end)



