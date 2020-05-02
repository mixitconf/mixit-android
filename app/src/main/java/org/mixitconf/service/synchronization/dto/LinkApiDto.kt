package org.mixitconf.service.synchronization.dto

import org.mixitconf.model.entity.Link
import org.mixitconf.model.enums.LinkType
import org.mixitconf.model.enums.Social


data class LinkApiDto(
        var name: String? = null, var url: String? = null
                     )

fun LinkApiDto.toEntity(userId: String): Link {
    val social = Social.values().firstOrNull { url!!.contains(it.pattern) }
    return Link(name!!, url!!, userId, if (social != null) LinkType.Social else LinkType.Web)
}