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
    KEYNOTE_SURPRISE(25),

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
enum class Room(val i18nId: Int, val capacity: Int, val filmed: Boolean=false, val risp:Boolean=false, val scribo:Boolean=false) {
    AMPHI1(R.string.amphi1, 500, filmed=true, risp = true),
    AMPHI2(R.string.amphi2, 200, filmed=true, risp = true),
    AMPHIC(R.string.amphic, 445, filmed=true, risp = true),
    AMPHID(R.string.amphid, 445, filmed=true, scribo = true),
    AMPHIK(R.string.amphik, 300, filmed=true, risp = true),
    SPEAKER(R.string.speaker, 16),
    ROOM1(R.string.room1, 198, filmed=true, scribo = true),
    ROOM2(R.string.room2, 108),
    ROOM3(R.string.room3, 30),
    ROOM4(R.string.room4, 30),
    ROOM5(R.string.room5, 30),
    ROOM6(R.string.room6, 30),
    ROOM7(R.string.room7, 30),
    MUMMY(R.string.mummy, 2),
    UNKNOWN(R.string.unknown, 0)
}

val Room.hardOfHearingSytem
        get() = this.risp || this.scribo
