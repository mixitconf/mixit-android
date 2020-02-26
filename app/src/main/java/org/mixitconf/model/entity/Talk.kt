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
    @PrimaryKey val id: String, val format: TalkFormat,
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
    val favorite: Boolean = false
) {
    // This list is only populated when we want to see the talk detail. For that we read speakers by their ids
    @Ignore
    val speakers: MutableList<Speaker> = mutableListOf()

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





