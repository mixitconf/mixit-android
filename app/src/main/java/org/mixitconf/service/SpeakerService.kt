package org.mixitconf.service

import android.content.Context
import android.widget.ImageView
import org.mixitconf.R
import org.mixitconf.model.Talk
import org.mixitconf.model.User
import org.mixitconf.repository.TalkReader
import org.mixitconf.repository.UserReader

/**
 * Created by devmind on 06/03/18.
 */

class SpeakerService(val context: Context) {


    fun findSpeakers(): List<User> {
        val talks = TalkReader.getInstance(context).findAll()
        return UserReader.getInstance(context).findByLogins(talks.flatMap { it.speakerIds }.toList())
    }

    fun findSpeakerImage(speakerImage: ImageView, speaker: User) {
        // Speaker images are downloaded on the app startup
        val imageResource = context.resources.getIdentifier(
                "mxt_speker_${if(speaker.lastname.isNullOrEmpty()) speaker.firstname.toSlug() else speaker.lastname.toSlug()}",
                "drawable",
                context.applicationInfo.packageName)

        speakerImage.setImageResource(if (imageResource > 0) imageResource else R.drawable.mxt_icon_unknown)
    }

    fun findSpeakerTalks(speaker: User): List<Talk> = TalkReader.getInstance(context).findAll().filter { it.speakerIds.contains(speaker.login) }.toList()

    companion object : SingletonHolder<SpeakerService, Context>(::SpeakerService)
}
