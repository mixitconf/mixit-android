package org.mixitconf.service.initialization.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.mixitconf.model.entity.Speaker
import org.mixitconf.model.enums.Language

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserDto(
    val login: String, val firstname: String, val lastname: String, val company: String?, val photoUrl: String?, val description: Map<Language, String> = emptyMap(), val links: List<LinkDto> = emptyList()
)

fun UserDto.toEntity() = Speaker(
    login, firstname, lastname, company, description.get(Language.FRENCH), description.get(Language.ENGLISH)
)