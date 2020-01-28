package org.mixitconf.service.initialization

import android.content.Context
import org.mixitconf.R
import org.mixitconf.addMinutes
import org.mixitconf.createDate
import org.mixitconf.model.enums.Language
import org.mixitconf.model.enums.Room
import org.mixitconf.model.enums.TalkFormat
import org.mixitconf.service.initialization.dto.TalkDto
import java.util.*

/**
 * TalkDto are read adjust Json file
 */
class TalkService(private val context: Context) {

    enum class Day(val day: Int) { One(23), Two(24), Three(25) }

    fun findNonTalkMoments(): List<TalkDto> = listOf(
        createNonTalkMoment(Day.One, TalkFormat.DAY, 8, 29, R.string.event_day1),
        createNonTalkMoment(Day.One, TalkFormat.WELCOME, 8, 30),
        createNonTalkMoment(Day.One, TalkFormat.ORGA, 9, 15),
        createNonTalkMoment(Day.One, TalkFormat.SESSION_INTRO, 10, 0),
        createNonTalkMoment(Day.One, TalkFormat.PAUSE_25_MIN, 10, 15),
        createNonTalkMoment(Day.One, TalkFormat.PAUSE_10_MIN, 11, 30),
        createNonTalkMoment(Day.One, TalkFormat.LUNCH, 12, 30),
        createNonTalkMoment(Day.One, TalkFormat.PAUSE_10_MIN, 15, 20),
        createNonTalkMoment(Day.One, TalkFormat.PAUSE_30_MIN, 16, 20),

        createNonTalkMoment(Day.Two, TalkFormat.DAY, 8, 29, R.string.event_day2),
        createNonTalkMoment(Day.Two, TalkFormat.WELCOME, 8, 30),
        createNonTalkMoment(Day.Two, TalkFormat.ORGA, 9, 15),
        createNonTalkMoment(Day.Two, TalkFormat.SESSION_INTRO, 10, 0),
        createNonTalkMoment(Day.Two, TalkFormat.PAUSE_25_MIN, 10, 15),
        createNonTalkMoment(Day.Two, TalkFormat.PAUSE_10_MIN, 11, 30),
        createNonTalkMoment(Day.Two, TalkFormat.LUNCH, 12, 30),
        createNonTalkMoment(Day.Two, TalkFormat.PAUSE_10_MIN, 15, 20),
        createNonTalkMoment(Day.Two, TalkFormat.PAUSE_30_MIN, 16, 20),

        createNonTalkMoment(Day.Three, TalkFormat.DAY, 8, 29, R.string.event_day3),
        createNonTalkMoment(Day.Three, TalkFormat.LUNCH, 12, 0)
    )

    private fun createNonTalkMoment(day: Day, talkFormat: TalkFormat, startHour: Int, startMinute: Int, title: Int = talkFormat.label) = TalkDto(
        talkFormat, "2019", context.getString(title), "", emptyList(), Language.FRENCH, Date(), "", "", Room.UNKNOWN, createDate(day.day, startHour, startMinute), createDate(day.day, startHour, startMinute).addMinutes(talkFormat.duration), id = day.name + talkFormat.name + startHour
    )


}




