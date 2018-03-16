package org.mixitconf.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_speaker_detail.*
import kotlinx.android.synthetic.main.fragment_speaker_detail_content.*
import org.mixitconf.R
import org.mixitconf.adapter.TalkListAdapter
import org.mixitconf.model.Language
import org.mixitconf.model.Social
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

        if (arguments == null) {
            throw IllegalStateException("SpeakerDetailFragment must be initialized with speaker id")
        }

        val speaker = UserReader.getInstance(context).findOne(arguments.getString(Utils.OBJECT_ID))

        speakerDetailName.setText("${speaker.firstname} ${speaker.lastname}".trim())
        speakerDetailCompany.setText(speaker.company)
        speakerDetailDescription.setText(speaker.description.get(Language.FRENCH)?.markdownToHtml())
        speakerDetailImage.setSpeakerImage(speaker)


        val mainSocial = Social.values().firstOrNull { social -> speaker.links.any { it.url.contains(social.pattern) }}
        val mainLink = if(mainSocial == null) speaker.links.firstOrNull() else speaker.links.first { it.url.contains(mainSocial.pattern) }

        if(mainLink ==null){
            navigation_speaker_link.visibility = View.GONE
        }
        else {
            navigation_speaker_link.setImageResource(if (mainSocial == null) R.drawable.mxt_icon_web else mainSocial.resourceId)
            navigation_speaker_link.setOnClickListener({ _ -> context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mainLink.url))) })
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
