package org.mixitconf.model.entity

import android.content.res.Resources
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.github.rjeschke.txtmark.Processor
import org.mixitconf.MiXiTApplication
import org.mixitconf.R
import org.mixitconf.model.enums.Language
import org.mixitconf.model.enums.Room
import org.mixitconf.model.enums.TalkFormat
import org.mixitconf.model.enums.isTalk
import org.mixitconf.toHtml
import java.text.DateFormat
import java.util.*

@Entity
data class Talk(
        @PrimaryKey val id: String,
        val format: TalkFormat,
        val event: String,
        val title: String,
        val summary: String,
        val speakerIds: String,
        val language: Language = Language.FRENCH,
        val description: String?,
        val topic: String,
        val room: Room,
        val start: Date,
        val end: Date,
        val favorite: Boolean = false) {
    // This list is only populated when we want to see the talk detail. For that we read speakers by their ids
    @Ignore
    val speakers: MutableList<Speaker> = mutableListOf()

    fun update(other: Talk) = this.copy(
            this.id, other.format, other.event, other.title, other.summary, other.speakerIds, other.language, other.description, other.topic, other.room, other.start, other.end, this.favorite
                                       )

    fun startSoon(favoriteNotificationDurationInMin: Long): Boolean {
        if (favoriteNotificationDurationInMin > 0) {
            val durationInMs = favoriteNotificationDurationInMin * 60 * 1000
            val now = Date().time
            return this.startLocaleTime > now && (this.startLocaleTime - now) < durationInMs
        }
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Talk

        if (id != other.id) return false
        if (format != other.format) return false
        if (event != other.event) return false
        if (title != other.title) return false
        if (summary != other.summary) return false
        if (speakerIds != other.speakerIds) return false
        if (language != other.language) return false
        if (description != other.description) return false
        if (topic != other.topic) return false
        if (room != other.room) return false
        if (start != other.start) return false
        if (end != other.end) return false
        if (speakers != other.speakers) return false

        return true
    }
}

val Talk.speakerIdList
    get() = speakerIds.split(",")

val Talk.topicDrawableResource
    get() = when (topic) {
        "aliens" -> R.drawable.mxt_topic_alien
        "design" -> R.drawable.mxt_topic_design
        "hacktivism" -> R.drawable.mxt_topic_hacktivism
        "learn" -> R.drawable.mxt_topic_learn
        "makers" -> R.drawable.mxt_topic_maker
        "team" -> R.drawable.mxt_topic_team
        "tech" -> R.drawable.mxt_topic_tech
        "ethics" -> R.drawable.mxt_topic_ethics
        "lifestyle" -> R.drawable.mxt_topic_lifestyle
        else -> R.drawable.mxt_topic_design
    }

val Talk.startLocale: Date
    get() = if (format.isTalk()) start.inFrenchLocale else start

val Talk.endLocale: Date
    get() = if (format.isTalk()) end.inFrenchLocale else end

val Talk.descriptionInMarkdown
    get() = if (description.isNullOrEmpty()) null else Processor.process(description).toHtml()

val Talk.summaryInMarkdown
    get() = if (summary.isEmpty()) "" else Processor.process(summary).toHtml()


val Talk.startLocaleTime
    get() = start.inFrenchLocale.time

val Talk.endLocaleTime
    get() = end.inFrenchLocale.time


private val Date.inFrenchLocale
    get(): Date {
        val calendar = Calendar.getInstance(Locale.FRANCE)
        calendar.time = this
        calendar.add(Calendar.HOUR, -2)
        return calendar.time
    }

fun Talk.getTimeLabel(resources: Resources): String = String.format(
        resources.getString(R.string.talk_time_range), MiXiTApplication.DATE_FORMAT.format(start), DateFormat.getTimeInstance(DateFormat.SHORT).format(startLocale), DateFormat.getTimeInstance(DateFormat.SHORT).format(endLocale)
                                                                   )

fun Talk.getBgColorDependingOnTime(color: Int): Int = if (Date().time > end.time) R.color.unknown else color





