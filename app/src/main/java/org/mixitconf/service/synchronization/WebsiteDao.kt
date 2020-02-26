package org.mixitconf.service.synchronization

import android.util.Log
import androidx.lifecycle.MutableLiveData
import org.mixitconf.service.synchronization.dto.UserApiDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WebsiteDao(private val websiteTalkService: WebsiteTalkService) {

    fun speakers() = MutableLiveData<List<UserApiDto>>().apply {
        websiteTalkService.speakers().enqueue(object : Callback<List<UserApiDto>> {

            override fun onResponse(call: Call<List<UserApiDto>?>?, response: Response<List<UserApiDto>?>?) {
                if (response != null && response.isSuccessful){
                    if(response.body().isNullOrEmpty()){
                        Log.w(this::javaClass.name, "No data returned on speaker api was called")
                    }
                    else {
                        this@apply.value = response.body()
                    }
                }
                else {
                    Log.d(this::javaClass.name, "No data returned on speaker api was called")
                }
            }

            override fun onFailure(call: Call<List<UserApiDto>?>?, t: Throwable?) {
                Log.e(this::javaClass.name, "Error when website speaker api was called", t)

            }
        })
    }

}