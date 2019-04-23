package org.mixitconf.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.mixitconf.model.Language
import org.mixitconf.model.entity.Speaker

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserDto(
    val login: String,
    val firstname: String,
    val lastname: String,
    val company: String?,
    val photoUrl: String?,
    val description: Map<Language, String> = emptyMap(),
    val links: List<LinkDto> = emptyList()
)

fun UserDto.toEntity() = Speaker(
    login,
    firstname,
    lastname,
    company,
    photoUrl,
    description.get(Language.FRENCH),
    description.get(Language.ENGLISH)
)