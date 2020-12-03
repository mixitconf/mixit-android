package org.mixitconf.service.synchronization.dto

import org.mixitconf.model.enums.WebsiteResponse
import org.mixitconf.model.enums.WebsiteResponseStatus

class WebsiteResponseDto(val status: Int, val message: String)

val WebsiteResponseDto.responseStatus
    get() = when (status) {
        200 -> WebsiteResponseStatus.OK
        400 -> WebsiteResponseStatus.BAD_REQUEST
        else -> WebsiteResponseStatus.INTERNAL_SERVER_ERROR
    }

val WebsiteResponseDto.response
    get() = when (message) {
        WebsiteResponse.CREDENTIAL_VALID.message -> WebsiteResponse.CREDENTIAL_VALID
        WebsiteResponse.TOKEN_SENT.message -> WebsiteResponse.TOKEN_SENT
        WebsiteResponse.INVALID_EMAIL.message -> WebsiteResponse.INVALID_EMAIL
        WebsiteResponse.INVALID_CREDENTIALS.message -> WebsiteResponse.INVALID_CREDENTIALS
        WebsiteResponse.EMAIL_NOT_KNOWN.message -> WebsiteResponse.EMAIL_NOT_KNOWN
        WebsiteResponse.EMAIL_SENT_ERROR.message -> WebsiteResponse.EMAIL_SENT_ERROR
        else -> WebsiteResponse.UNKNOWN_ERROR
    }

