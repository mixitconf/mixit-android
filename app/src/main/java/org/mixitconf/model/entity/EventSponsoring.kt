package org.mixitconf.model.entity

import androidx.room.Entity
import org.mixitconf.model.enums.SponsorshipLevel

@Entity(primaryKeys = ["sponsorId", "eventId", "level"])
data class EventSponsoring(
    val sponsorId: String, val eventId: String, val level: SponsorshipLevel
)


