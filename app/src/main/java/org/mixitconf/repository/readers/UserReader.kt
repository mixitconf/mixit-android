package org.mixitconf.repository.readers

import org.mixitconf.model.dto.UserDto

/**
 * Speakers are read from Json file
 */
class UserReader(private val users: List<UserDto>) {

    fun findAll(): List<UserDto> = users

}