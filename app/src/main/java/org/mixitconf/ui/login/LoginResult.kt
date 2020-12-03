package org.mixitconf.ui.login

import org.mixitconf.model.enums.WebsiteResponse

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
        val response: WebsiteResponse,
        val login: String? = null,
        val firstname: String? = null,
        val lastname: String? = null)

