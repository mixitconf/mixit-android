package org.mixitconf.repository

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.mixitconf.R
import org.mixitconf.SingletonHolder
import org.mixitconf.model.Talk

/**
 * Talk are read from Json file
 */
class TalkReader(private val context: Context){

    val objectMapper:ObjectMapper = jacksonObjectMapper()

    private fun readFile(): List<Talk>{
        val jsonInputStream = context.resources.openRawResource(R.raw.talks_2018)
        if(TalkReader.talks.isEmpty()){
            val talks: List<Talk> = objectMapper.readValue(jsonInputStream)
            talks.forEach { TalkReader.talks.add(it) }
        }
        return TalkReader.talks

    }

    fun findAll(): List<Talk> = readFile()

    fun findOne(id: String): Talk = readFile().filter { it.id == id }.first()

    companion object : SingletonHolder<TalkReader, Context>(::TalkReader){
        val talks:MutableList<Talk> = mutableListOf()
    }
}


