package org.mixitconf.mixitconf.service

import android.content.Context
import org.mixitconf.mixitconf.SingletonHolder
import org.mixitconf.mixitconf.model.User
import org.mixitconf.mixitconf.repository.TalkReader
import org.mixitconf.mixitconf.repository.UserReader

/**
 * Created by devmind on 06/03/18.
 */

class SpeakerService(val context: Context) {


    fun findSpeakers(): List<User> {
        val talks = TalkReader.getInstance(context).findAll()
        return UserReader.getInstance(context).findByLogins(talks.flatMap { it.speakerIds }.toList())
    }

    companion object : SingletonHolder<SpeakerService, Context>(::SpeakerService)
}
