package org.mixitconf.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_talk_detail.*
import kotlinx.android.synthetic.main.fragment_talk_detail_content.*
import org.mixitconf.R
import org.mixitconf.adapter.UserListAdapter
import org.mixitconf.model.Language
import org.mixitconf.model.Talk
import org.mixitconf.model.hardOfHearingSytem
import org.mixitconf.service.*


class TalkDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_talk_detail, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        check(arguments != null && context != null)
        { "TalkDetailFragment must be initialized with talk id" }

        val talk = mixitApp.talkReader.findOne(arguments!!.getString(Constant.OBJECT_ID))
        val calendarLoader = CalendarLoader(mixitApp, talk)

        talkName.text = talk.title
        talkSummary.text = talk.summary.markdownToHtml()
        talkDescription.text = talk.description?.markdownToHtml()
        talkTime.text = talk.getTimeLabel(mixitApp.resources)
        talkRoom.setText("${resources.getText(talk.room.i18nId)} (cap. ${talk.room.capacity})")
        talkTopic.text = talk.topic
        talkImageTrack.setImageResource(talk.getTopicDrawableResource())
        talkLanguage.text = if (talk.language == Language.ENGLISH) "EN" else "FR"

        if(talk.room.hardOfHearingSytem){
            talkDescHearing.text = resources.getText(if(talk.room.scribo) R.string.hearing_scrivo else R.string.hearing_risp)
        }
        else{
            talkDescHearing.visibility = View.GONE
            talkHearing.visibility = View.GONE
        }

        if(!talk.room.filmed){
            talkVideo.visibility = View.GONE
        }

        // Adds speaker
        val speakers = mixitApp.userReader.findByLogins(talk.speakerIds)

        // Lookup the recycler view in activity layout
        talkSpeakerList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = UserListAdapter(activity as SpeakerFragment.OnSpeakerSelectedListener).also {
                it.update(speakers)
            }
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        navigation_calendar_add.setOnClickListener {
            if (!mixitApp.hasPermission(Manifest.permission.WRITE_CALENDAR)) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR), 0)
            } else {
                val hasConcurrentEvent =
                    calendarLoader.hasConcurrentEventInCalendar != null && calendarLoader.hasConcurrentEventInCalendar!!

                // If we have an existing event in user calendar, we have to ask him if he wants to insert or not a new one
                AlertDialog.Builder(context)
                    .setTitle(R.string.calendar_add)
                    .setMessage(if (hasConcurrentEvent) R.string.calendar_question2 else R.string.calendar_question1)
                    .setPositiveButton(android.R.string.yes) { _, _ -> insertEventInCalendar(talk) }
                    .setNegativeButton(android.R.string.no) { _, _ -> }
                    .show()
            }
        }

        if (mixitApp.hasPermission(Manifest.permission.READ_CALENDAR)) {
            LoaderManager.getInstance(this).initLoader(0, null, calendarLoader)
        }
    }

    private fun insertEventInCalendar(talk: Talk) = context!!.startActivity(
        Intent(Intent.ACTION_INSERT)
            .setType("vnd.android.cursor.item/event")
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, talk.start.timeInFrenchLocale)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, talk.end.timeInFrenchLocale)
            .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
            .putExtra(CalendarContract.Events.TITLE, "[mixit19] : ${talk.title}")
            .putExtra(CalendarContract.Events.EVENT_LOCATION, resources.getString(talk.room.i18nId))
            .putExtra(CalendarContract.Events.ALLOWED_REMINDERS, CalendarContract.Reminders.METHOD_ALARM)
            .putExtra(CalendarContract.Events.DESCRIPTION, talk.summary)
    )
}
