package org.mixitconf.repository.sync

import org.mixitconf.model.dto.EventDto
import org.mixitconf.model.dto.TalkDto
import org.mixitconf.model.dto.UserDto
import retrofit2.Call
import retrofit2.http.GET


interface MiXiTAppService {

    @GET("speaker")
    fun speakers(): Call<List<UserDto>>

    @GET("talk")
    fun talks(): Call<List<TalkDto>>

    @GET("event")
    fun event(): Call<EventDto>
}
