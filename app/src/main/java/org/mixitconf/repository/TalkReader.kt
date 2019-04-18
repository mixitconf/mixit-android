package org.mixitconf.repository

import android.content.Context
import org.mixitconf.R
import org.mixitconf.model.Language
import org.mixitconf.model.Room
import org.mixitconf.model.Talk
import org.mixitconf.model.TalkFormat
import org.mixitconf.service.addMinutes
import org.mixitconf.service.createDate
import java.util.*

/**
 * Talk are read adjust Json file
 */
class TalkReader(val talks: List<Talk>, private val context: Context) {

    fun findAll(): List<Talk> = talks

    fun findOne(id: String): Talk = talks.first { it.id == id }

    fun findMarkers(): List<Talk> = listOf(
        createNonTalkMomentFirstDay(TalkFormat.DAY, 8, 0, R.string.event_day1),

        createNonTalkMomentFirstDay(TalkFormat.ORGA, 8, 45, R.string.planning_team_word),
        createNonTalkMomentFirstDay(TalkFormat.SESSION_INTRO, 9, 45, R.string.planning_session_intro),
        createNonTalkMomentFirstDay(TalkFormat.PAUSE_20_MIN, 9, 50, R.string.planning_pause),
        createNonTalkMomentFirstDay(TalkFormat.PAUSE_10_MIN, 11, 0, R.string.planning_pause),
        createNonTalkMomentFirstDay(TalkFormat.LUNCH, 12, 0, R.string.planning_lunch),
        createNonTalkMomentFirstDay(TalkFormat.SESSION_INTRO, 13, 35, R.string.planning_session_intro),
        createNonTalkMomentFirstDay(TalkFormat.PAUSE_10_MIN, 13, 40, R.string.planning_pause),
        createNonTalkMomentFirstDay(TalkFormat.RANDOM, 13, 50, R.string.planning_random),
        createNonTalkMomentFirstDay(TalkFormat.PAUSE_10_MIN, 15, 0, R.string.planning_pause),
        createNonTalkMomentFirstDay(TalkFormat.PAUSE_20_MIN, 16, 0, R.string.planning_pause),
        createNonTalkMomentFirstDay(TalkFormat.PARTY, 19, 30, R.string.planning_pause),

        createNonTalkMomentSecondDay(TalkFormat.DAY, 8, 0, R.string.event_day2),

        createNonTalkMomentSecondDay(TalkFormat.ORGA, 9, 0, R.string.planning_team_word),
        createNonTalkMomentSecondDay(TalkFormat.SESSION_INTRO, 9, 35, R.string.planning_session_intro),
        createNonTalkMomentSecondDay(TalkFormat.PAUSE_30_MIN, 9, 40, R.string.planning_pause),
        createNonTalkMomentSecondDay(TalkFormat.PAUSE_10_MIN, 11, 0, R.string.planning_pause),
        createNonTalkMomentSecondDay(TalkFormat.LUNCH, 12, 0, R.string.planning_lunch),
        createNonTalkMomentSecondDay(TalkFormat.SESSION_INTRO, 13, 35, R.string.planning_session_intro),
        createNonTalkMomentSecondDay(TalkFormat.PAUSE_10_MIN, 13, 40, R.string.planning_pause),
        createNonTalkMomentSecondDay(TalkFormat.RANDOM, 13, 50, R.string.planning_random),
        createNonTalkMomentSecondDay(TalkFormat.PAUSE_10_MIN, 15, 0, R.string.planning_pause),
        createNonTalkMomentSecondDay(TalkFormat.PAUSE_20_MIN, 16, 0, R.string.planning_pause),
        createNonTalkMomentSecondDay(TalkFormat.SESSION_INTRO, 17, 35, R.string.planning_team_outro)
    )

    private fun createNonTalkMomentFirstDay(
        talkFormat: TalkFormat,
        startHour: Int,
        startMinute: Int,
        title: Int? = null
    ) =
        createNonTalkMoment(talkFormat, title, 23, startHour, startMinute)

    private fun createNonTalkMomentSecondDay(
        talkFormat: TalkFormat,
        startHour: Int,
        startMinute: Int,
        title: Int? = null
    ) =
        createNonTalkMoment(talkFormat, title, 24, startHour, startMinute)

    private fun createNonTalkMoment(talkFormat: TalkFormat, title: Int?, day: Int, startHour: Int, startMinute: Int) =
        Talk(
            talkFormat,
            "2019",
            if (title == null) "" else context.getString(title),
            "", emptyList(), Language.FRENCH, Date(), "",
            "",
            Room.UNKNOWN,
            createDate(day, startHour, startMinute),
            createDate(day, startHour, startMinute).addMinutes(talkFormat.duration), ""
        )


}




