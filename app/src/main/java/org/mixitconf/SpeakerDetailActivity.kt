package org.mixitconf

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import com.github.rjeschke.txtmark.Processor
import kotlinx.android.synthetic.main.activity_speakers.*
import org.mixitconf.adapter.UserListAdapter
import org.mixitconf.model.Language
import org.mixitconf.repository.TalkReader
import org.mixitconf.repository.UserReader

class SpeakerDetailActivity : AbstractMixitActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    companion object {
        val SPEAKER_ID = "talkId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk_detail)
        speakersNavigation.setOnNavigationItemSelectedListener(getNavigationItemSelectedListener())

        val talk = TalkReader.getInstance(this).findOne(intent.getStringExtra(SPEAKER_ID))

        findViewById<TextView>(R.id.talk_name).apply {
            setText(talk.title)
        }
        findViewById<TextView>(R.id.talk_summary).apply {
            setText(Html.fromHtml(Processor.process(talk.summary)))
        }
        findViewById<TextView>(R.id.talk_descrition).apply {
            setText(if (talk.description != null) Html.fromHtml(Processor.process(talk.description)) else "")
        }
        findViewById<TextView>(R.id.talk_room).apply {
            setText(talk.title)
        }
        findViewById<TextView>(R.id.talk_room).apply {
            setText(context.resources.getIdentifier(talk.room.name.toLowerCase(), "string", context.applicationInfo.packageName))
        }
        findViewById<TextView>(R.id.talk_topic).apply {
            setText(talk.topic)
        }
        findViewById<ImageView>(R.id.talk_image_track).apply {
            setImageResource(talk.getTopicDrawableRessource())
        }
        findViewById<ImageView>(R.id.talk_image_language).apply {
            setImageResource(if (talk.language == Language.ENGLISH) R.drawable.mxt_flag_en else R.drawable.mxt_flag_fr)
        }


        // Adds speaker
        val speakers = UserReader.getInstance(this).findByLogins(talk.speakerIds)
        viewManager = LinearLayoutManager(this)
        viewAdapter = UserListAdapter(SpeakerActivity.UserOnClickListener(this), speakers, this)

        // Lookup the recyclerview in activity layout
        recyclerView = findViewById<RecyclerView>(R.id.speaker_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

    }
}