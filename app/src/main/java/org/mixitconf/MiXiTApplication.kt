package org.mixitconf

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.mixitconf.model.Event
import org.mixitconf.model.Talk
import org.mixitconf.model.User
import org.mixitconf.repository.EventReader
import org.mixitconf.repository.TalkReader
import org.mixitconf.repository.UserReader
import org.mixitconf.service.SpeakerService

class MiXiTApplication : Application() {


    val speakerService by lazy {
        SpeakerService(talkReader, userReader)
    }

    val eventReader by lazy {
        val jsonInputStream = applicationContext.resources.openRawResource(R.raw.events)
        val events: List<Event> = jacksonObjectMapper().readValue(jsonInputStream)
        EventReader(events)
    }

    val talkReader by lazy {
        val jsonInputStream = applicationContext.resources.openRawResource(R.raw.talks_2019)
        val talks: List<Talk> = jacksonObjectMapper().readValue(jsonInputStream)
        TalkReader(talks, applicationContext)
    }

    val userReader by lazy {
        val json = applicationContext.resources.openRawResource(R.raw.users)
        val users: List<User> = jacksonObjectMapper().readValue(json)
        UserReader(users)
    }

    fun hasPermission(permission: String) = ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED

}

