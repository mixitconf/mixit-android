package org.mixitconf.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.mixitconf.model.LinkType
import org.mixitconf.model.Social
import java.util.*

@Entity
data class Link(
    val name: String,
    val url: String,
    val speakerId: String,
    val linkType: LinkType,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)

val Link.socialType
    get() = Social.values().first{ social -> url.contains(social.pattern) }