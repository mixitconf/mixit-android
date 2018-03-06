package org.mixitconf.mixitconf.repository

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.mixitconf.mixitconf.R
import org.mixitconf.mixitconf.model.User

/**
 * Speakers are read from Json file
 */
class UserReader {

    val objectMapper: ObjectMapper = jacksonObjectMapper()

    private fun readFile(context: Context): List<User>{
        val jsonInputStream = context.resources.openRawResource(R.raw.users)
        val events: List<User> = objectMapper.readValue(jsonInputStream)
        return events
    }

    fun findAll(context: Context): List<User> = readFile(context)

    fun findOne(context: Context, login: String): User = readFile(context).filter { it.login == login }.first()

    fun findByLogins(context: Context, logins: List<String>): List<User> = readFile(context).filter { logins.contains(it.login) }
}