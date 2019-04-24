package org.mixitconf.model.entity

import androidx.room.*
import org.mixitconf.model.enums.SponsorshipLevel

@Entity(primaryKeys = arrayOf("sponsorId","level"))
data class EventSponsoring(
    val sponsorId: String,
    val eventId: String,
    val level: SponsorshipLevel
)


