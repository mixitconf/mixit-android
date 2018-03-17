package org.mixitconf.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
        val login: String,
        val firstname: String,
        val lastname: String,
        val company: String?,
        val photoUrl: String?,
        val description: Map<Language, String> = emptyMap(),
        val links: List<Link> = emptyList()
)