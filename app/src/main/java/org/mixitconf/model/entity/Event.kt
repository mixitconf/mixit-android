package org.mixitconf.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Event(
    @PrimaryKey val id: String, val start: Date, val end: Date, val year: Int
)
