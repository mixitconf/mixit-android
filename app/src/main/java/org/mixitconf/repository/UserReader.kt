package org.mixitconf.repository

import org.mixitconf.model.User

/**
 * Speakers are read from Json file
 */
class UserReader(val users: List<User>) {

    fun findAll(): List<User> = users

    fun findOne(login: String): User = users.first { it.login == login }

    fun findByLogins(logins: List<String>): List<User> = users.filter { logins.contains(it.login) }

}