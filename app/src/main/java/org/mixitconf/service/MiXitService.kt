package org.mixitconf.service

import android.app.IntentService
import android.os.Handler
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.mixitconf.mixitApp
import retrofit2.Call
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext


abstract class MiXitService(name: String?) : IntentService(name), CoroutineScope {

    private val mHandler = Handler()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    // Helper for showing tests
    fun toast(text: CharSequence) {
        mHandler.post { Toast.makeText(mixitApp, text, Toast.LENGTH_LONG).show() }
    }

    /**
     * Launch a retrofit request and analyze response
     */
    fun <T> callApi(call: Call<List<T>>, errorMessage: Int, callback: (List<T>) -> Unit) {
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                if (response.body()!!.isEmpty()) {
                    toast(String.format(mixitApp.getText(errorMessage).toString(), "no data"))
                }
                callback(response.body()!!)
            } else {
                toast(String.format(mixitApp.getText(errorMessage).toString(), response.errorBody()))
            }
        } catch (e: UnknownHostException) {
            toast(String.format(mixitApp.getText(errorMessage).toString(), "Host not available. Try later"))
        } catch (e: RuntimeException) {
            toast(String.format(mixitApp.getText(errorMessage).toString(), e.message))
        }
    }

}
