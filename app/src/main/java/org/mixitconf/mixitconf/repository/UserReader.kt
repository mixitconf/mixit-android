package org.mixitconf.mixitconf.repository

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.mixitconf.mixitconf.R
import org.mixitconf.mixitconf.SingletonHolder
import org.mixitconf.mixitconf.model.User

/**
 * Speakers are read from Json file
 */
class UserReader(private val context: Context) {

    val objectMapper: ObjectMapper = jacksonObjectMapper()

    private fun readFile(): List<User>{
        val jsonInputStream = context.resources.openRawResource(R.raw.users)
        val events: List<User> = objectMapper.readValue(jsonInputStream)
        return events
    }

    fun findAll(): List<User> = readFile()

    fun findOne(login: String): User = readFile().filter { it.login == login }.first()

    fun findByLogins(logins: List<String>): List<User> = readFile().filter { logins.contains(it.login) }

    companion object : SingletonHolder<UserReader, Context>(::UserReader)
}