package org.mixitconf.service

import android.app.IntentService
import android.os.Handler
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.mixitconf.mixitApp
import retrofit2.Call
import java.lang.Exception
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext


abstract class MiXitService(name: String?) : IntentService(name), CoroutineScope {

    private val mHandler = Handler()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default


    /**
     * Launch a retrofit request and analyze response
     */
    fun <T> callApi(call: Call<List<T>>, errorMessage: Notification, callback: (List<T>) -> Unit) {
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                if (response.body()!!.isEmpty()) {
                    NotificationService.startNotification(mixitApp, errorMessage, "no data")
                }
                callback(response.body()!!)
            } else {
                NotificationService.startNotification(mixitApp, errorMessage, response.errorBody().toString())
            }
        } catch (e: UnknownHostException) {
            NotificationService.startNotification(mixitApp, errorMessage, "Host not available. Try later")
        } catch (e: Exception) {
            NotificationService.startNotification(mixitApp, errorMessage,  e.message)
        }
    }

}
