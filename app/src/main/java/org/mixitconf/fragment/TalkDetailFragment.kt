package org.mixitconf.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_talk_detail.*
import kotlinx.android.synthetic.main.fragment_talk_detail_content.*
import org.mixitconf.R
import org.mixitconf.adapter.UserListAdapter
import org.mixitconf.model.Language
import org.mixitconf.model.Talk
import org.mixitconf.repository.TalkReader
import org.mixitconf.repository.UserReader
import org.mixitconf.service.*


class TalkDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments == null || context == null) {
            throw IllegalStateException("TalkDetailFragment must be initialized with talk id")
        }

        val talk = TalkReader.getInstance(context!!).findOne(arguments!!.getString(Utils.OBJECT_ID))
        val calendarLoader = CalendarLoader(context!!, talk)

        talkName.text = talk.title
        talkSummary.text = talk.summary.markdownToHtml()
        talkDescription.text = talk.description?.markdownToHtml()
        talkTime.text = talk.getTimeLabel(context!!)
        talkRoom.setText(talk.getRoomLabel(context!!))
        talkTopic.text = talk.topic
        talkImageTrack.setImageResource(talk.getTopicDrawableResource())
        talkLanguage.visibility = if (talk.language == Language.ENGLISH) View.VISIBLE else View.GONE

        // Adds speaker
        val speakers = UserReader.getInstance(context!!).findByLogins(talk.speakerIds)

        // Lookup the recyclerview in activity layout
        talkSpeakerList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = UserListAdapter(speakers, context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        navigation_calendar_add.setOnClickListener({ _ ->
            if (!context!!.hasPermission(Manifest.permission.WRITE_CALENDAR)) {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR), 0)
            }
            else{
                val hasConcurrentEvent = calendarLoader.hasConcurrentEventInCalendar != null && calendarLoader.hasConcurrentEventInCalendar!!

                // If we have an existing event in user calendar, we have to ask him if he wants to insert or not a new one
                AlertDialog.Builder(context)
                        .setTitle(R.string.calendar_add)
                        .setMessage(if(hasConcurrentEvent) R.string.calendar_question2 else R.string.calendar_question1)
                        .setPositiveButton(android.R.string.yes) { _, _ -> insertEventInCalendar(talk) }
                        .setNegativeButton(android.R.string.no) { _, _ ->  }
                        .show()

            }

        })

        if (context!!.hasPermission(Manifest.permission.READ_CALENDAR)) {
            loaderManager.initLoader(0, null, calendarLoader)
        }
    }

    private fun insertEventInCalendar(talk:Talk) = context!!.startActivity(Intent(Intent.ACTION_INSERT)
            .setType("vnd.android.cursor.item/event")
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, talk.start.time)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, talk.end.time)
            .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
            .putExtra(CalendarContract.Events.TITLE, "[mixit18] : ${talk.title}")
            .putExtra(CalendarContract.Events.EVENT_LOCATION, context!!.getString(talk.getRoomLabel(context!!)))
            .putExtra(CalendarContract.Events.ALLOWED_REMINDERS, CalendarContract.Reminders.METHOD_ALARM)
            .putExtra(CalendarContract.Events.DESCRIPTION, talk.summary))
}
