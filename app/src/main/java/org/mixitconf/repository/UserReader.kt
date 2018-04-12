package org.mixitconf.repository

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.mixitconf.R
import org.mixitconf.model.User
import org.mixitconf.service.SingletonHolder

/**
 * Speakers are read from Json file
 */
class UserReader(val users: List<User>) {

    fun findAll(): List<User> = users

    fun findOne(login: String): User = users.first { it.login == login }

    fun findByLogins(logins: List<String>): List<User> = users.filter { logins.contains(it.login) }

    companion object : SingletonHolder<UserReader, Context>({
        val json = it.resources.openRawResource(R.raw.users)
        val users : List<User> = jacksonObjectMapper().readValue(json)
        UserReader(users)
    })
}