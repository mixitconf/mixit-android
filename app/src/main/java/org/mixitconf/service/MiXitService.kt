package org.mixitconf.service

import android.app.IntentService
import android.os.Handler
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.mixitconf.mixitApp
import retrofit2.Call
import java.lang.Exception
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext


abstract class MiXitService(name: String?) : IntentService(name), CoroutineScope {

    companion object{
        const val TAG = "MiXitService"
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default


    /**
     * Launch a retrofit request and analyze response
     */
    fun <T> callApi(call: Call<List<T>>, errorMessage: Notification, backgroundProcess: Boolean, callback: (List<T>) -> Unit) {
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                if (response.body()!!.isEmpty()) {
                    log(errorMessage, backgroundProcess, "no data")
                }
                callback(response.body()!!)
            } else {
                log(errorMessage, backgroundProcess, response.errorBody().toString())
            }
        } catch (e: UnknownHostException) {
            log(errorMessage, backgroundProcess, "Host not available. Try later")
        } catch (e: Exception) {
            log(errorMessage, backgroundProcess, e.message)
        }
    }

    /**
     * When task is launched by a background service we don't want to add a notification for each error
     */
    private fun log(errorMessage: Notification, backgroundProcess: Boolean, message: String?){
        if(backgroundProcess) {
            Log.e("MiXitService", message)
        }
        else{
            NotificationService.startNotification(mixitApp, errorMessage,  message)
        }
    }

}
