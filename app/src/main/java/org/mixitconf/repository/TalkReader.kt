package org.mixitconf.repository

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.mixitconf.R
import org.mixitconf.model.Language
import org.mixitconf.model.Room
import org.mixitconf.model.Talk
import org.mixitconf.model.TalkFormat
import org.mixitconf.service.SingletonHolder
import org.mixitconf.service.addMinutes
import org.mixitconf.service.adjust
import org.mixitconf.service.toLocale
import java.util.*

/**
 * Talk are read adjust Json file
 */
class TalkReader(private val context: Context) {

    val objectMapper: ObjectMapper = jacksonObjectMapper()

    companion object : SingletonHolder<TalkReader, Context>(::TalkReader) {
        private val talks: MutableList<Talk> = mutableListOf()
    }

    private fun readFile(): List<Talk> {
        val jsonInputStream = context.resources.openRawResource(R.raw.talks_2018)
        if (TalkReader.talks.isEmpty()) {
            val talks: List<Talk> = objectMapper.readValue(jsonInputStream)
            talks.forEach { TalkReader.talks.add(transformDate(it)) }
        }
        return TalkReader.talks

    }

    fun findAll(): List<Talk> = readFile()

    fun findOne(id: String): Talk = readFile().filter { it.id == id }.first()

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

            createNonTalkMomentSecondDay(TalkFormat.DAY, 8, 0, R.string.event_day1),

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

    private fun createNonTalkMomentFirstDay(talkFormat: TalkFormat, startHour: Int, startMinute: Int, title: Int? = null) =
            createNonTalkMoment(talkFormat, title, 19, startHour, startMinute)

    private fun createNonTalkMomentSecondDay(talkFormat: TalkFormat, startHour: Int, startMinute: Int, title: Int? = null) =
            createNonTalkMoment(talkFormat, title, 20, startHour, startMinute)

    private fun createNonTalkMoment(talkFormat: TalkFormat, title: Int?, day: Int, startHour: Int, startMinute: Int) = Talk(
            talkFormat, "2018",
            if (title == null) "" else context.getString(title),
            "", emptyList(), Language.FRENCH, Date(), "",
            "", Room.UNKNOWN,
            Date().adjust(day, startHour, startMinute),
            Date().adjust(day, startHour, startMinute).addMinutes(talkFormat.duration), ""
    )

    private fun transformDate(talk: Talk) = Talk(talk.format, talk.event, talk.title, talk.summary, talk.speakerIds,
            talk.language, talk.addedAt, talk.description, talk.topic, talk.room, talk.start.toLocale(),
            talk.end.toLocale(), talk.id)


}




