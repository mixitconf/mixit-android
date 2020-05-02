package org.mixitconf.service.synchronization

import org.mixitconf.service.synchronization.dto.SpeakerApiDto
import org.mixitconf.service.synchronization.dto.TalkApiDto
import retrofit2.Call
import retrofit2.http.GET

interface WebsiteTalkService {

    /**
     * Read speaker list for current edition
     */
    @GET("speaker")
    fun speakers(): Call<List<SpeakerApiDto>>

    /**
     * Read talk list for current edition
     */
    @GET("talk")
    fun talks(): Call<List<TalkApiDto>>
}