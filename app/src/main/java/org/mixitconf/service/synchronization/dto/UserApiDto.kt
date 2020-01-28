package org.mixitconf.service.synchronization.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.mixitconf.model.entity.Speaker
import org.mixitconf.model.enums.Language

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserApiDto(
    var login: String? = null, var firstname: String? = null, var lastname: String? = null, var company: String? = null, var photoUrl: String? = null, var description: Map<Language, String> = emptyMap(), var links: List<LinkApiDto> = emptyList()
)

fun UserApiDto.toEntity() = Speaker(
    login!!, firstname!!, lastname!!, company, description.get(Language.FRENCH), description.get(Language.ENGLISH)
)