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
){
    fun getTopicDrawableRessource(): Int = when(topic){
        "aliens" -> R.drawable.mxt_topic_alien
        "design" -> R.drawable.mxt_topic_design
        "hacktivism" -> R.drawable.mxt_topic_hacktivism
        "learn" -> R.drawable.mxt_topic_learn
        "makers" -> R.drawable.mxt_topic_maker
        "team" -> R.drawable.mxt_topic_team
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
    DAY(0),
    TIME(0);

    fun isConference() = this == TALK || this == WORKSHOP || this == KEYNOTE
}

@Suppress("UNUSED_PARAMETER")
enum class Room(val capacity: Int, val color: Int) {
    A_NOT_APPLICABLE(0, android.R.color.black),
    AMPHI1(500, R.color.room1),
    AMPHI2(200, R.color.room2),
    ROOM1(110, R.color.room3),
    ROOM2(110, R.color.room4),
    ROOM3(30, R.color.room5),
    ROOM4(30, R.color.room6),
    ROOM5(30, R.color.room7),
    ROOM6(30, R.color.room8),
    ROOM7(50, R.color.room9),
    UNKNOWN(0, R.color.unknown);
}