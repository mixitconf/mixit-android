package org.mixitconf.service.initialization.dto

import androidx.room.PrimaryKey
import org.mixitconf.model.entity.EventSponsoring
import org.mixitconf.model.enums.SponsorshipLevel
import java.util.*

data class EventSponsoringDto(@PrimaryKey val sponsorId: String, val level: SponsorshipLevel, val subscriptionDate: Date)

fun EventSponsoringDto.toEntity(eventId: String) = EventSponsoring(sponsorId, eventId, level)
