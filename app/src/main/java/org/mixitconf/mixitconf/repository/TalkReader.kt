package org.mixitconf.mixitconf.repository

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.mixitconf.mixitconf.R
import org.mixitconf.mixitconf.model.Talk

/**
 * Talk are read from Json file
 */
class TalkReader{

    val objectMapper:ObjectMapper = jacksonObjectMapper()

    private fun readFile(context:Context): List<Talk>{
        val jsonInputStream = context.resources.openRawResource(R.raw.talks_2018)
        val talks: List<Talk> = objectMapper.readValue(jsonInputStream)
        return talks
    }

    fun findAll(context:Context): List<Talk> = readFile(context)

    fun findOne(context:Context, id: String): Talk = readFile(context).filter { it.id == id }.first()
}


