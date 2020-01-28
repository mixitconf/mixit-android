package org.mixitconf.service.synchronization.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.mixitconf.model.entity.Talk
import org.mixitconf.model.enums.Language
import org.mixitconf.model.enums.Room
import org.mixitconf.model.enums.TalkFormat
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class TalkApiDto(
    var format: TalkFormat? = null,
    var event: String? = null,
    var title: String? = null,
    var summary: String? = null,
    var speakerIds: List<String>? = null,
    var language: Language = Language.FRENCH,
    var addedAt: Date? = null,
    var description: String? = null,
    var topic: String? = null,
    var room: Room? = null,
    var start: Date? = null,
    var end: Date? = null,
    var id: String? = null
)

fun TalkApiDto.toEntity() = Talk(
    id!!, format!!, event!!, title!!, summary!!, speakerIds!!.joinToString(","), language, description, topic!!, room!!, start!!, end!!
)



