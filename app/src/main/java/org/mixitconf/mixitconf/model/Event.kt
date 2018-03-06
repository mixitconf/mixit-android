package org.mixitconf.mixitconf.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class Event(
        val id: String,
        val start: Date,
        val end: Date,
        val current: Boolean = false,
        val sponsors: List<EventSponsoring> = emptyList(),
        val photoUrls: List<Link> = emptyList(),
        val year:Int
)

data class EventSponsoring(
        val level: SponsorshipLevel,
        val sponsorId: String,
        val subscriptionDate: Date
)

enum class SponsorshipLevel {
    GOLD,
    SILVER,
    BRONZE,
    LANYARD,
    PARTY,
    BREAKFAST,
    LUNCH,
    HOSTING,
    VIDEO,
    COMMUNITY,
    MIXTEEN,
    ECOCUP,
    ACCESSIBILITY,
    NONE
}

