package org.mixitconf

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_talk_detail.*
import org.mixitconf.adapter.UserListAdapter
import org.mixitconf.model.Language
import org.mixitconf.repository.TalkReader
import org.mixitconf.repository.UserReader
import org.mixitconf.service.Utils
import org.mixitconf.service.markdownToHtml
import java.text.DateFormat

class TalkDetailActivity : AbstractMixitActivity() {

    companion object {
        val TALK_ID = "talkId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk_detail)

        val talk = TalkReader.getInstance(this).findOne(intent.getStringExtra(TALK_ID))

        talkName.setText(talk.title)
        talkSummary.setText(talk.summary.markdownToHtml())
        talkDescrition.setText(talk.description?.markdownToHtml())
        talkTime.setText(String.format(resources.getString(R.string.talk_time_range),
                Utils.DATE_FORMAT.format(talk.start),
                DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.start),
                DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.end)))
        talkRoom.setText(this.resources.getIdentifier(talk.room.name.toLowerCase(), "string", this.applicationInfo.packageName))
        talkTopic.setText(talk.topic)

        talkImageTrack.setImageResource(talk.getTopicDrawableRessource())
        talkImageLanguage.setImageResource(if (talk.language == Language.ENGLISH) R.drawable.mxt_flag_en else R.drawable.mxt_flag_fr)


        // Adds speaker
        val speakers = UserReader.getInstance(this).findByLogins(talk.speakerIds)

        // Lookup the recyclerview in activity layout
        talkSpeakerList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = UserListAdapter(speakers, context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

    }
}
