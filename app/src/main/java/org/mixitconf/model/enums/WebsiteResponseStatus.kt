package org.mixitconf.model.enums

enum class WebsiteResponseStatus {
    OK,
    INTERNAL_SERVER_ERROR,
    BAD_REQUEST
}

enum class WebsiteResponse(val status: WebsiteResponseStatus, val message: String) {
    CREDENTIAL_VALID(WebsiteResponseStatus.OK, "Credentials are valids"),
    TOKEN_SENT(WebsiteResponseStatus.OK, "A token was send by email. Please check your mailbox and send it in the future request"),
    INVALID_EMAIL(WebsiteResponseStatus.INTERNAL_SERVER_ERROR, "Email is invalid"),
    INVALID_CREDENTIALS(WebsiteResponseStatus.BAD_REQUEST, "Credentials are invalid"),
    EMAIL_NOT_KNOWN(WebsiteResponseStatus.BAD_REQUEST, "This email is not known. You have to create an account on our website if you want to use this functionnality"),
    EMAIL_SENT_ERROR(WebsiteResponseStatus.INTERNAL_SERVER_ERROR, "An expected error occured on email sent"),
    UNKNOWN_ERROR(WebsiteResponseStatus.INTERNAL_SERVER_ERROR,"")
}
