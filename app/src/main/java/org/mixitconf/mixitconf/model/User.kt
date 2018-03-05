package org.mixitconf.mixitconf.model

data class User(
        val login: String,
        val firstname: String,
        val lastname: String,
        val company: String,
        val description: Map<Language, String> = emptyMap(),
        val photoUrl: String,
        val links: List<Link> = emptyList()
)

enum class Role {
    STAFF,
    STAFF_IN_PAUSE,
    USER
}