package org.mixitconf

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_speaker_detail.*
import org.mixitconf.adapter.TalkListAdapter
import org.mixitconf.model.Language
import org.mixitconf.model.Link
import org.mixitconf.repository.UserReader
import org.mixitconf.service.SpeakerService
import org.mixitconf.service.markdownToHtml
import org.mixitconf.service.toHtml

class SpeakerDetailActivity : AbstractMixitActivity() {

    companion object {
        val SPEAKER_LOGIN = "login"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speaker_detail)
        speakerNavigation.setOnNavigationItemSelectedListener(getNavigationItemSelectedListener())

        val speaker = UserReader.getInstance(this).findOne(intent.getStringExtra(SPEAKER_LOGIN))

        SpeakerService.getInstance(this).findSpeakerImage(speakerDetailImage, speaker)
        speakerDetailName.setText("${speaker.firstname} ${speaker.lastname}".trim())
        speakerDetailCompany.setText(speaker.company)
        speakerDetailDescription.setText(speaker.description.get(Language.FRENCH)?.markdownToHtml())

        // Adds links
        speakerDetailLinkList.apply {
            speaker.links.forEach {
                val textView = TextView(context)
                textView.setText(String.format("<a href=\"%s\">%s</a>", it.url, it.name).toHtml())
                textView.setOnClickListener(OnLinkClickListener(it, context))
                addView(textView)
            }
        }

        // Adds talks
        val talks = SpeakerService.getInstance(this).findSpeakerTalks(speaker)
        // Lookup the recyclerview in activity layout
        speakerDetailTalkList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = TalkListAdapter(talks, context)
        }


    }

    class OnLinkClickListener(val link: Link, val context: Context) : View.OnClickListener {
        override fun onClick(view: View?) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link.url))
            context.startActivity(intent)
        }

    }
}
