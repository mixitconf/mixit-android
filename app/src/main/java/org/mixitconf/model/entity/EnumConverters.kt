package org.mixitconf.model.entity

import androidx.room.TypeConverter
import org.mixitconf.model.enums.*
import java.util.*

class EnumConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toLanguage(value: String?): Language? {
        return value?.let { Language.valueOf(it) }
    }

    @TypeConverter
    fun fromLanguage(value: Language?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toLinkType(value: String?): LinkType? {
        return value?.let { LinkType.valueOf(it) }
    }

    @TypeConverter
    fun fromLinkType(value: LinkType?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toTalkFormat(value: String?): TalkFormat? {
        return value?.let { TalkFormat.valueOf(it) }
    }

    @TypeConverter
    fun fromTalkFormat(value: TalkFormat?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toRoom(value: String?): Room? {
        return value?.let { Room.valueOf(it) }
    }

    @TypeConverter
    fun fromRoom(value: Room?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toSponsorshipLevel(value: String?): SponsorshipLevel? {
        return value?.let { SponsorshipLevel.valueOf(it) }
    }

    @TypeConverter
    fun fromSponsorshipLevel(value: SponsorshipLevel?): String? {
        return value?.toString()
    }
}