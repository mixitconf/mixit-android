package org.mixitconf.service

import android.content.Context
import org.mixitconf.model.Talk
import org.mixitconf.model.User
import org.mixitconf.repository.TalkReader
import org.mixitconf.repository.UserReader

class SpeakerService(val context: Context) {


    fun findSpeakers(): List<User> {
        val talks = TalkReader.getInstance(context).findAll()
        return UserReader.getInstance(context).findByLogins(talks.flatMap { it.speakerIds })
    }

    fun findSpeakerTalks(speaker: User): List<Talk> = TalkReader.getInstance(context)
            .findAll()
            .filter { it.speakerIds.contains(speaker.login) }

    companion object : SingletonHolder<SpeakerService, Context>(::SpeakerService)
}
