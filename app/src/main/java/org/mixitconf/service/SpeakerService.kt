package org.mixitconf.service

import android.content.Context
import android.os.Environment
import android.widget.ImageView
import com.squareup.picasso.Picasso
import org.mixitconf.R
import org.mixitconf.model.User
import org.mixitconf.repository.TalkReader
import org.mixitconf.repository.UserReader
import java.io.File

/**
 * Created by devmind on 06/03/18.
 */

class SpeakerService(val context: Context) {


    fun findSpeakers(): List<User> {
        val talks = TalkReader.getInstance(context).findAll()
        return UserReader.getInstance(context).findByLogins(talks.flatMap { it.speakerIds }.toList())
    }


    fun findSpeakerImage(speakerImage: ImageView, speaker: User){
        // Speaker images are downloaded on the app startup
        val image = File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "speaker_${speaker.firstname}_${speaker.lastname}")

        if (image.exists()) {
            Picasso
                    .with(context)
                    .load(image)
                    .resizeDimen(R.dimen.item_image_size, R.dimen.item_image_size)
                    .placeholder(R.drawable.mxt_icon_unknown)
                    .into(speakerImage)
        } else {
            speakerImage.setImageResource(R.drawable.mxt_icon_unknown)
        }
    }

    companion object : SingletonHolder<SpeakerService, Context>(::SpeakerService)
}
