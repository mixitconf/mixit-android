package org.mixitconf.repository.readers

import android.content.Context
import org.mixitconf.R
import org.mixitconf.model.Language
import org.mixitconf.model.Room
import org.mixitconf.model.TalkFormat
import org.mixitconf.model.dto.TalkDto
import org.mixitconf.service.addMinutes
import org.mixitconf.service.createDate
import java.util.*

/**
 * TalkDto are read adjust Json file
 */
class TalkReader(private val talks: List<TalkDto>, private val context: Context) {

    enum class Day(val day: Int) { one(23), two(24), three(25) }

    fun findAll(): List<TalkDto> = talks + findMarkers()

    fun findMarkers(): List<TalkDto> =
        listOf(
            createNonTalkMoment(Day.one, TalkFormat.DAY, 8, 29, R.string.event_day1),
            createNonTalkMoment(Day.one, TalkFormat.WELCOME, 8, 30),
            createNonTalkMoment(Day.one, TalkFormat.ORGA, 9, 15),
            createNonTalkMoment(Day.one, TalkFormat.SESSION_INTRO, 10, 0),
            createNonTalkMoment(Day.one, TalkFormat.PAUSE_25_MIN, 10, 15),
            createNonTalkMoment(Day.one, TalkFormat.PAUSE_10_MIN, 11, 30),
            createNonTalkMoment(Day.one, TalkFormat.LUNCH, 12, 30),
            createNonTalkMoment(Day.one, TalkFormat.PAUSE_10_MIN, 15, 20),
            createNonTalkMoment(Day.one, TalkFormat.PAUSE_30_MIN, 16, 20),

            createNonTalkMoment(Day.two, TalkFormat.DAY, 8, 29, R.string.event_day2),
            createNonTalkMoment(Day.two, TalkFormat.WELCOME, 8, 30),
            createNonTalkMoment(Day.two, TalkFormat.ORGA, 9, 15),
            createNonTalkMoment(Day.two, TalkFormat.SESSION_INTRO, 10, 0),
            createNonTalkMoment(Day.two, TalkFormat.PAUSE_25_MIN, 10, 15),
            createNonTalkMoment(Day.two, TalkFormat.PAUSE_10_MIN, 11, 30),
            createNonTalkMoment(Day.two, TalkFormat.LUNCH, 12, 30),
            createNonTalkMoment(Day.two, TalkFormat.PAUSE_10_MIN, 15, 20),
            createNonTalkMoment(Day.two, TalkFormat.PAUSE_30_MIN, 16, 20),

            createNonTalkMoment(Day.three, TalkFormat.DAY, 8, 29, R.string.event_day3),
            createNonTalkMoment(Day.three, TalkFormat.LUNCH, 12, 0)
        )


    private fun createNonTalkMoment(day: Day, talkFormat: TalkFormat, startHour: Int, startMinute: Int, title: Int = talkFormat.label) =
        TalkDto(
            talkFormat,
            "2019",
            context.getString(title),
            "",
            emptyList(),
            Language.FRENCH,
            Date(),
            "",
            "",
            Room.UNKNOWN,
            createDate(day.day, startHour, startMinute),
            createDate(day.day, startHour, startMinute).addMinutes(talkFormat.duration),
            id = UUID.randomUUID().toString()
        )


}




