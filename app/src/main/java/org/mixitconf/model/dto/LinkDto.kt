package org.mixitconf.model.dto

import org.mixitconf.R
import org.mixitconf.model.LinkType
import org.mixitconf.model.Social
import org.mixitconf.model.entity.Link


data class LinkDto(
    val name: String,
    val url: String
)

fun LinkDto.toEntity(userId: String): Link {
    val social = Social.values().firstOrNull { url.contains(it.pattern) }
    return Link(name, url, userId, if(social==null) LinkType.Social else LinkType.Web)
}