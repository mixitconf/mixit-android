package org.mixitconf.service

import org.mixitconf.model.Talk
import org.mixitconf.model.User
import org.mixitconf.repository.TalkReader
import org.mixitconf.repository.UserReader

class SpeakerService(val talkReader: TalkReader, val userReader: UserReader) {

    fun findSpeakers(): List<User> {
        val talks = talkReader.findAll()
        return userReader.findByLogins(talks.flatMap { it.speakerIds })
    }

    fun findSpeakerTalks(speaker: User): List<Talk> = talkReader.findAll().filter { it.speakerIds.contains(speaker.login) }

}
