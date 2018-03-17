package org.mixitconf.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.mixitconf.R
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class Talk(
        val format: TalkFormat,
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
        val id: String
) {
    fun getTopicDrawableResource(): Int = when (topic) {
        "aliens" -> R.drawable.mxt_topic_alien
        "design" -> R.drawable.mxt_topic_design
        "hacktivism" -> R.drawable.mxt_topic_hacktivism
        "learn" -> R.drawable.mxt_topic_learn
        "makers" -> R.drawable.mxt_topic_maker
        "team" -> R.drawable.mxt_topic_team
        "tech" -> R.drawable.mxt_topic_tech
        else -> R.drawable.mxt_topic_design
    }
}

enum class TalkFormat(val duration: Int) {
    TALK(50),
    WORKSHOP(110),
    RANDOM(25),
    KEYNOTE(25),

    SESSION_INTRO(5),
    PAUSE_10_MIN(10),
    PAUSE_20_MIN(20),
    PAUSE_30_MIN(30),
    PARTY(210),
    LUNCH(70),
    ORGA(10),
    DAY(0);
}

@Suppress("UNUSED_PARAMETER")
enum class Room { AMPHI1, AMPHI2, ROOM1, ROOM2, ROOM3, ROOM4, ROOM5, ROOM6, ROOM7, UNKNOWN }