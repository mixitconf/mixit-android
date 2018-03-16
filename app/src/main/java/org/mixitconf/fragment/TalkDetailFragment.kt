package org.mixitconf.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_talk_detail.*
import org.mixitconf.R
import org.mixitconf.adapter.UserListAdapter
import org.mixitconf.model.Language
import org.mixitconf.repository.TalkReader
import org.mixitconf.repository.UserReader
import org.mixitconf.service.Utils
import org.mixitconf.service.markdownToHtml
import java.text.DateFormat


class TalkDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(arguments==null){
            throw IllegalStateException("TalkDetailFragment must be initialized with talk id")
        }

        val talk = TalkReader.getInstance(context).findOne(arguments.getString(Utils.OBJECT_ID))

        talkName.setText(talk.title)
        talkSummary.setText(talk.summary.markdownToHtml())
        talkDescrition.setText(talk.description?.markdownToHtml())
        talkTime.setText(String.format(resources.getString(R.string.talk_time_range),
                Utils.DATE_FORMAT.format(talk.start),
                DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.start),
                DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.end)))
        talkRoom.setText(this.resources.getIdentifier(talk.room.name.toLowerCase(), "string", context.applicationInfo.packageName))
        talkTopic.setText(talk.topic)

        talkImageTrack.setImageResource(talk.getTopicDrawableRessource())
        talkImageLanguage.setImageResource(if (talk.language == Language.ENGLISH) R.drawable.mxt_flag_en else R.drawable.mxt_flag_fr)


        // Adds speaker
        val speakers = UserReader.getInstance(context).findByLogins(talk.speakerIds)

        // Lookup the recyclerview in activity layout
        talkSpeakerList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = UserListAdapter(speakers, context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}
