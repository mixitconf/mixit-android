package org.mixitconf.mixitconf.model

import java.time.LocalDateTime

data class Talk(
        val format: TalkFormat,
        val event: String,
        val title: String,
        val summary: String,
        val speakerIds: List<String>,
        val language: Language = Language.FRENCH,
        val addedAt: LocalDateTime,
        val description: String,
        val topic: String,
        val room: Room,
        val start: LocalDateTime,
        val end: LocalDateTime,
        val id: String
)

enum class TalkFormat(val duration: Int) {
    TALK(50),
    LIGHTNING_TALK(5),
    WORKSHOP(110),
    RANDOM(25),
    KEYNOTE(25)
}

@Suppress("UNUSED_PARAMETER")
enum class Room(capacity: Int) {
    AMPHI1(500),
    AMPHI2(200),
    ROOM1(110),
    ROOM2(110),
    ROOM3(30),
    ROOM4(30),
    ROOM5(30),
    ROOM6(30),
    ROOM7(50),
    UNKNOWN(0);
}