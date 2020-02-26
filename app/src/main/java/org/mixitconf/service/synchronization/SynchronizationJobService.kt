package org.mixitconf.service.synchronization

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mixitconf.MiXiTApplication
import org.mixitconf.mixitApp
import kotlin.coroutines.CoroutineContext


class SynchronizationJobService : JobService(), CoroutineScope {

    companion object{
        const val TAG = "SynchronizationJob"
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "End job to refresh MiXiT application data")
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Start job to refresh MiXiT application data")
        launch {
            Intent(applicationContext, SynchronizationService::class.java).also { intent ->
                intent.putExtra(SynchronizationService.AUTOMATIC_TASK, true)
                startService(intent)
            }
            // We schedule a new execution if parameter has not been updated
            PreferenceManager.getDefaultSharedPreferences(mixitApp).apply {
                val userWantsAutomaticUpdate = this.getBoolean(MiXiTApplication.PREF_DATA_SYNC, true)
                if (userWantsAutomaticUpdate) {
                    MiXiTApplication.scheduleAutomaticDataUpdate(applicationContext)
                }
            }
        }
        return false
    }

}
