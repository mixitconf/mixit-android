package org.mixitconf.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_speaker_detail.*
import org.mixitconf.R
import org.mixitconf.adapter.TalkListAdapter
import org.mixitconf.model.Language
import org.mixitconf.repository.UserReader
import org.mixitconf.service.SpeakerService
import org.mixitconf.service.Utils
import org.mixitconf.service.markdownToHtml
import org.mixitconf.service.setSpeakerImage


class SpeakerDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_speaker_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(arguments==null){
            throw IllegalStateException("SpeakerDetailFragment must be initialized with speaker id")
        }

        val speaker = UserReader.getInstance(context).findOne(arguments.getString(Utils.OBJECT_ID))

        speakerDetailName.setText("${speaker.firstname} ${speaker.lastname}".trim())
        speakerDetailCompany.setText(speaker.company)
        speakerDetailDescription.setText(speaker.description.get(Language.FRENCH)?.markdownToHtml())
        speakerDetailImage.setSpeakerImage(speaker)

        // Adds links
        speakerDetailLinkList.apply {
            speaker.links.forEach {
                val button = Button(context)
                button.setText(it.name)
                button.setOnClickListener({ _ -> context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.url))) })
                button.setBackgroundResource(R.drawable.button_selector)
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(0, 0, 15, 0)
                button.layoutParams = params
                addView(button)
            }
        }

        // Adds talks
        val talks = SpeakerService.getInstance(context).findSpeakerTalks(speaker)
        // Lookup the recyclerview in activity layout
        speakerDetailTalkList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = TalkListAdapter(talks, context)
        }
    }

}
