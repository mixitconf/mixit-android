package org.mixitconf.model.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.mixitconf.model.entity.*

@Database(entities = [Link::class, Event::class, Talk::class, Speaker::class, EventSponsoring::class],
          version = 1,
          exportSchema = true)
@TypeConverters(EnumConverters::class)
abstract class MiXiTDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun eventSponsoringDao(): EventSponsoringDao
    abstract fun talkDao(): TalkDao
    abstract fun speakerDao(): SpeakerDao
    abstract fun linkDao(): LinkDao
}