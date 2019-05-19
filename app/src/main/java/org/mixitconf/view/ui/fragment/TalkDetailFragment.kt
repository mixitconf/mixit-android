package org.mixitconf.view.ui.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.loader.app.LoaderManager
import kotlinx.android.synthetic.main.fragment_talk_detail.*
import kotlinx.android.synthetic.main.fragment_talk_detail_content.*
import org.mixitconf.*
import org.mixitconf.view.ui.OnSpeakerSelectedListener
import org.mixitconf.view.adapter.SpeakerListAdapter
import org.mixitconf.model.enums.Language
import org.mixitconf.model.entity.*
import org.mixitconf.model.enums.hardOfHearingSytem
import org.mixitconf.service.calendar.CalendarLoader
import org.mixitconf.viewmodel.TalkDetailViewModel


class TalkDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_talk_detail, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        talkSpeakerList.default { SpeakerListAdapter(activity as OnSpeakerSelectedListener) }

        val model = ViewModelProviders.of(this).get(TalkDetailViewModel::class.java)

        model.liveData.observe(this, Observer { talk ->
            val calendarLoader = CalendarLoader(mixitApp, talk)

            talkName.text = talk.title
            talkSummary.text = talk.summaryInMarkdown
            talkDescription.text = talk.descriptionInMarkdown
            talkTime.text = talk.getTimeLabel(mixitApp.resources)

            talkRoom.text = resources.getText(R.string.roomandcapa).toString().format(resources.getText(talk.room.i18nId), talk.room.capacity)
            talkTopic.text = talk.topic
            talkImageTrack.setImageResource(talk.topicDrawableResource)
            talkLanguage.text = if (talk.language == Language.ENGLISH) "EN" else "FR"

            talkVideo.visibility = talk.room.filmed.visibility
            talkDescVideo.visibility = talk.room.filmed.visibility
            talkHearing.visibility = talk.room.hardOfHearingSytem.visibility
            talkDescHearing.visibility = talk.room.hardOfHearingSytem.visibility
            talkDescHearing.text =
                resources.getText(if (talk.room.scribo) R.string.hearing_scrivo else if (talk.room.risp) R.string.hearing_risp else R.string.unknown)

            if(talk.room.scriboUrl.isNullOrEmpty()){
                buttonTranscription.visibility = View.GONE
            }
            else {
                buttonTranscription.visibility = View.VISIBLE
                buttonTranscription.setOnClickListener{
                    startActivity(Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(talk.room.scriboUrl)
                    ))
                }
            }

            (talkSpeakerList.adapter as SpeakerListAdapter).update(talk.speakers)

            navigation_calendar_add.setOnClickListener { calendarAddEvent(calendarLoader, talk) }

            if (mixitApp.hasPermission(Manifest.permission.READ_CALENDAR)) {
                LoaderManager.getInstance(this).initLoader(0, null, calendarLoader)
            }
        })

        model.loadSpeaker(arguments?.getString(MiXiTApplication.OBJECT_ID) ?: "")
    }

    private fun calendarAddEvent(calendarLoader: CalendarLoader, talk: Talk) {
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

    private fun insertEventInCalendar(talk: Talk) = context!!.startActivity(
        Intent(Intent.ACTION_INSERT)
            .setType("vnd.android.cursor.item/event")
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, talk.startLocaleTime)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, talk.endLocaleTime)
            .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
            .putExtra(CalendarContract.Events.TITLE, "[mixit19] : ${talk.title}")
            .putExtra(CalendarContract.Events.EVENT_LOCATION, resources.getString(talk.room.i18nId))
            .putExtra(CalendarContract.Events.ALLOWED_REMINDERS, CalendarContract.Reminders.METHOD_ALARM)
            .putExtra(CalendarContract.Events.DESCRIPTION, talk.summary)
    )

    override fun onStop() {
        super.onStop()
        navigation_calendar_add.setOnClickListener(null)
        buttonTranscription.setOnClickListener(null)
    }
}
