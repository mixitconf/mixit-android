package org.mixitconf

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import com.github.rjeschke.txtmark.Processor
import kotlinx.android.synthetic.main.activity_talk_detail.*
import org.mixitconf.adapter.UserListAdapter
import org.mixitconf.model.Language
import org.mixitconf.repository.TalkReader
import org.mixitconf.repository.UserReader
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TalkDetailActivity : AbstractMixitActivity() {

    companion object {
        val TALK_ID = "talkId"
        val DATE_FORMAT = SimpleDateFormat("EEE", Locale.getDefault())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk_detail)
        talkNavigation.setOnNavigationItemSelectedListener(getNavigationItemSelectedListener())

        val talk = TalkReader.getInstance(this).findOne(intent.getStringExtra(TALK_ID))

        talkName.setText(talk.title)
        talkSummary.setText(Html.fromHtml(Processor.process(talk.summary)))
        talkDescrition.setText(if (talk.description != null) Html.fromHtml(Processor.process(talk.description)) else "")
        talkTime.setText(String.format(resources.getString(R.string.talk_time_range),
                DATE_FORMAT.format(talk.start),
                DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.start),
                DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.end)))
        talkRoom.setText(this.resources.getIdentifier(talk.room.name.toLowerCase(), "string", this.applicationInfo.packageName))
        talkTopic.setText(talk.topic)

        talkImageTrack.setImageResource(talk.getTopicDrawableRessource())
        talkImageLanguage.setImageResource(if (talk.language == Language.ENGLISH) R.drawable.mxt_flag_en else R.drawable.mxt_flag_fr)


        // Adds speaker
        val speakers = UserReader.getInstance(this).findByLogins(talk.speakerIds)
        val dataAdapter = UserListAdapter(SpeakerActivity.UserOnClickListener(this), speakers, this)

        // Lookup the recyclerview in activity layout
        talkSpeakerList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = dataAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

    }
}
