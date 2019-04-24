package org.mixitconf.service.calendar

import android.database.Cursor
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events.CONTENT_URI
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import org.mixitconf.MiXiTApplication
import org.mixitconf.model.entity.Talk

/**
 * Loader used to find MiXiT talks in the user Calendar. This loader is only launched if user accepts to open
 * his calendar to the app. When we read the calendar we only search if one event overlaps the current talk.
 * This aim is to not insert more than once the talk in the user calendar
 */
class CalendarLoader(val app: MiXiTApplication, val talk: Talk) : LoaderManager.LoaderCallbacks<Cursor> {

    var hasConcurrentEventInCalendar: Boolean? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val request = "(${CalendarContract.Events.DTSTART} <= ?) AND  (${CalendarContract.Events.DTEND} >= ?)"
        val requestArgs = arrayOf("${talk.start.time}", "${talk.end.time}")

        return CursorLoader(app.applicationContext, CONTENT_URI, arrayOf("_id"), request, requestArgs, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        // No data are read in calendar events. We just see if data are found
        hasConcurrentEventInCalendar = data != null && data.moveToNext()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        // N/A
    }

}
