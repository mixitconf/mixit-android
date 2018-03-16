package org.mixitconf.service

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events.CONTENT_URI
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import org.mixitconf.model.Talk

/**
 * Loader used to find MiXiT talks in the user Calendar. This loader is only launched if user accepts to open
 * his calendar to the app. When we read the calendar we only search if one event overlaps the currrent talk.
 * This aim is to not insert more than once the talk in the user calendar
 */
class CalendarLoader(val context: Context, val talk: Talk) : LoaderManager.LoaderCallbacks<Cursor> {

    var hasConcurrentEventInCalendar: Boolean? = null

    /**
     * Value read in user Calendar
     */
    val DATA_PROJECTION = arrayOf("_id")

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor>? {
        // User can choose to not use this feature
        if (!context.hasPermission(Manifest.permission.READ_CALENDAR)) {
            return null
        }
        val request = "(${CalendarContract.Events.DTSTART} <= ?) AND  (${CalendarContract.Events.DTEND} >= ?)"
        val requestArgs = arrayOf("${talk.start.time}", "${talk.end.time}")

        return CursorLoader(context, CONTENT_URI, DATA_PROJECTION, request, requestArgs, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        // No data are read in calendar events. We just see if data are found
        hasConcurrentEventInCalendar = if (data != null && data.moveToNext()) true else false
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        // N/A
    }

}
