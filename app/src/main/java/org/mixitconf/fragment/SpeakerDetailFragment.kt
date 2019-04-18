package org.mixitconf.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_speaker_detail.*
import kotlinx.android.synthetic.main.fragment_speaker_detail_content.*
import org.mixitconf.OnTalkSelectedListener
import org.mixitconf.R
import org.mixitconf.adapter.TalkListAdapter
import org.mixitconf.model.Language
import org.mixitconf.model.Social
import org.mixitconf.service.*


class SpeakerDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_speaker_detail, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        check(arguments != null && context != null && activity != null)
        { "SpeakerDetailFragment must be initialized with speaker id" }

        val speaker = mixitApp.userReader.findOne(arguments!!.getString(Constant.OBJECT_ID))

        speaker.apply {
            speakerDetailName.text = speaker.fullname()
            speakerDetailCompany.text = speaker.company
            speakerDetailDescription.text = speaker.description[Language.FRENCH]?.markdownToHtml()
            speakerDetailImage.setSpeakerImage(speaker)
        }


        val mainSocial: Social? = Social.values().firstOrNull { social -> speaker.links.any { it.url.contains(social.pattern) } }
        val mainLink = if (mainSocial == null) speaker.links.firstOrNull() else speaker.links.first { it.url.contains(mainSocial.pattern) }
        val mainResource = mainSocial?.resourceId ?: R.drawable.mxt_icon_web

        if (mainLink == null) {
            navigation_speaker_link.setVisibility(View.GONE)
        } else {
            navigation_speaker_link.setImageResource(mainResource)
            navigation_speaker_link.setOnClickListener({ _ -> context!!.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mainLink.url))) })
        }

        // Adds talks
        val talks = mixitApp.speakerService.findSpeakerTalks(speaker)
        // Lookup the recyclerview in activity layout
        speakerDetailTalkList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = TalkListAdapter(activity as OnTalkSelectedListener, mixitApp.resources).also {
                it.update(talks)
            }
        }
    }
}


