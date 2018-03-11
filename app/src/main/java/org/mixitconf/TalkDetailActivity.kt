package org.mixitconf

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
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

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    companion object {
        val TALK_ID = "talkId"
        val DATE_FORMAT = SimpleDateFormat("EEE", Locale.getDefault())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk_detail)
        talkNavigation.setOnNavigationItemSelectedListener(getNavigationItemSelectedListener())

        val talk = TalkReader.getInstance(this).findOne(intent.getStringExtra(TALK_ID))

        findViewById<TextView>(R.id.talk_name).apply {
            setText(talk.title)
        }
        findViewById<TextView>(R.id.talk_summary).apply {
            setText(Html.fromHtml(Processor.process(talk.summary)))
        }
        findViewById<TextView>(R.id.talk_descrition).apply {
            setText(if (talk.description != null) Html.fromHtml(Processor.process(talk.description)) else "")
        }
        findViewById<TextView>(R.id.talk_time).apply {
            setText(String.format(resources.getString(R.string.talk_time_range),
                    DATE_FORMAT.format(talk.start),
                    DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.start),
                    DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.end)))
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
        recyclerView = findViewById<RecyclerView>(R.id.speakerList).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

    }
}
