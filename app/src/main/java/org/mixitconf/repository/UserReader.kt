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
class UserReader(private val context: Context) {

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    private fun readFile(): List<User>{
        val jsonInputStream = context.resources.openRawResource(R.raw.users)
        if(UserReader.users.isEmpty()){
            val users: List<User> = objectMapper.readValue(jsonInputStream)
            users.forEach { UserReader.users.add(it) }
        }
        return UserReader.users
    }

    fun findAll(): List<User> = readFile()

    fun findOne(login: String): User = readFile().first { it.login == login }

    fun findByLogins(logins: List<String>): List<User> = readFile().filter { logins.contains(it.login) }

    companion object : SingletonHolder<UserReader, Context>(::UserReader){
        private val users:MutableList<User> = mutableListOf()
    }
}