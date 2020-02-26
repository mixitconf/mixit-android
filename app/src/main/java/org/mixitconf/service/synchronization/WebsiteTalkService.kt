package org.mixitconf.service.synchronization

import org.mixitconf.service.synchronization.dto.TalkApiDto
import org.mixitconf.service.synchronization.dto.UserApiDto
import retrofit2.Call
import retrofit2.http.GET

interface WebsiteTalkService {

    /**
     * Read speaker list for current edition
     */
    @GET("speaker")
    fun speakers(): Call<List<UserApiDto>>

    /**
     * Read talk list for current edition
     */
    @GET("talk")
    fun talks(): Call<List<TalkApiDto>>
}