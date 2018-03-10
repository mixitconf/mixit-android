package org.mixitconf.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import com.github.rjeschke.txtmark.Processor
import org.mixitconf.R
import org.mixitconf.model.Language
import org.mixitconf.model.Talk
import org.mixitconf.repository.UserReader
import org.mixitconf.service.HtmlTagHandler
import org.mixitconf.service.SpeakerService
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Display the talk detail
 */
class TalkDetailFragment : Fragment() {

    val htmlTagHandler = HtmlTagHandler()
    val viewBuilder: ViewBuilder by lazy { ViewBuilder(view!!) }

    var mInflater: LayoutInflater? = null

    companion object {
        val DATE_FORMAT = SimpleDateFormat("EEE", Locale.getDefault())
        val TALK_ID = "talkId"
        val NAME = "name"
        val SUMMARY = "summary"
        val DESCRIPTION = "descrition"
        val ROOM = "room"
        val ROOM_COLOR = "roomColor"
        val TOPIC = "topic"
        val SPEAKERS = "speakers"
        val TOPIC_IMAGE = "topicImage"
        val LANG_IMAGE = "languageImage"
        val START = "start"
        val END = "end"
        val TIME = "time"

        fun newInstance(talk: Talk): TalkDetailFragment = TalkDetailFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(TALK_ID, talk.id)
                        putString(NAME, talk.title)
                        putString(SUMMARY, talk.summary)
                        putString(START, DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.start))
                        putString(END, DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.end))
                        putString(TIME, DATE_FORMAT.format(talk.start))
                        if (talk.description != null) {
                            putString(DESCRIPTION, talk.description)
                        }
                        putString(ROOM, talk.room.name)
                        putInt(ROOM_COLOR, talk.room.color)
                        putString(TOPIC, talk.topic)
                        putStringArrayList(SPEAKERS, ArrayList(talk.speakerIds))
                        putInt(TOPIC_IMAGE, talk.getTopicDrawableRessource())
                        putInt(LANG_IMAGE, if (talk.language == Language.ENGLISH) R.drawable.mxt_flag_en else R.drawable.mxt_flag_fr)
                    }
                }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.mInflater = inflater
        return inflater!!.inflate(R.layout.fragment_talk_detail, container, false) as ViewGroup
    }

    inner class ViewBuilder(val view: View) {
        val timeTxtView: TextView
        val nameTxtView: TextView
        val summaryTxtView: TextView
        val descritionTxtView: TextView
        val roomTxtView: TextView
        val topicTxtView: TextView
        val topicImageView: ImageView
        val languageImageView: ImageView
        val talkView: View
        val speakerTitleTxtView: TextView
        val speakerListLayout: LinearLayout

        var speakerImage: ImageView? = null
        var speakerName: TextView? = null
        var speakerBio: TextView? = null

        init {
            timeTxtView = view.findViewById(R.id.time)
            nameTxtView = view.findViewById(R.id.talk_name)
            summaryTxtView = view.findViewById(R.id.talk_summary)
            descritionTxtView = view.findViewById(R.id.talk_descrition)
            roomTxtView = view.findViewById(R.id.talk_room)
            talkView = view.findViewById(R.id.talkView)
            speakerTitleTxtView = view.findViewById(R.id.speakersTitle)
            speakerListLayout = view.findViewById(R.id.speaker_list)
            languageImageView = view.findViewById(R.id.talk_image_language)
            topicTxtView = view.findViewById(R.id.talk_topic)
            topicImageView = view.findViewById(R.id.talk_image_track)
        }
    }

    /**
     * Recuperation des marques de la partie en cours
     */
    override fun onResume() {
        super.onResume()

        if (arguments.getString(TALK_ID).isEmpty()) {
            //(activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.homeFragment, TalkFragment.newInstance()).commit()
        }

        with(viewBuilder) {
            nameTxtView.setText(arguments.getString(NAME))
            timeTxtView.setText(String.format(resources.getString(R.string.talk_time_range), arguments.getString(TIME), arguments.getString(START), arguments.getString(END)))
            summaryTxtView.setText(Html.fromHtml(Processor.process(arguments.getString(SUMMARY))))
            descritionTxtView.setText(if (arguments.getString(DESCRIPTION) != null) Html.fromHtml(Processor.process(arguments.getString(DESCRIPTION))) else "")
            topicTxtView.setText(String.format(resources.getString(R.string.topic), arguments.getString(TOPIC)))
            topicImageView.setImageResource(arguments.getInt(TOPIC_IMAGE))
            languageImageView.setImageResource(arguments.getInt(LANG_IMAGE))
            roomTxtView.setText(arguments.getString(ROOM))
            roomTxtView.setBackgroundColor(context.resources.getColor(arguments.getInt(ROOM_COLOR)))

            // Adds a table layout
            if (mInflater != null) {
                val tableParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
                val tableLayout = TableLayout(activity.baseContext)
                tableLayout.layoutParams = tableParams

                UserReader.getInstance(context).findByLogins(arguments.getStringArrayList(SPEAKERS)).forEach {
                    val row = mInflater!!.inflate(R.layout.activity_talk_item, tableLayout, false) as LinearLayout

                    speakerImage = row.findViewById(R.id.speaker_image)
                    speakerName = view.findViewById(R.id.speaker_name)
                    speakerBio = view.findViewById(R.id.speaker_bio)

                    SpeakerService.getInstance(context).findSpeakerImage(speakerImage!!, it)
                    speakerName!!.setText("${it.firstname} ${it.lastname}".trim())
                    speakerBio!!.setText("${if (it.company == null) "" else it.company}")
                }
            }
        }

    }
}
