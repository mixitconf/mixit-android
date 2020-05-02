package org.mixitconf.ui.speaker.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_speaker_detail.*
import kotlinx.android.synthetic.main.fragment_speaker_detail_content.*
import org.mixitconf.MiXiTApplication
import org.mixitconf.R
import org.mixitconf.default
import org.mixitconf.mixitApp
import org.mixitconf.model.entity.*
import org.mixitconf.ui.adapter.TalkListAdapter
import org.mixitconf.ui.OnTalkSelectedListener


class SpeakerDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_speaker_detail, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        speakerDetailTalkList.default { TalkListAdapter(activity as OnTalkSelectedListener, mixitApp.resources) }

        val model = ViewModelProvider(this).get(SpeakerDetailViewModel::class.java)

        model.liveData.observe(viewLifecycleOwner, Observer { speaker ->
            speakerDetailName.text = speaker.fullname
            speakerDetailCompany.text = speaker.company
            speakerDetailDescription.text = speaker.descriptionInMarkdown
            speakerDetailImage.setSpeakerImage(speaker)

            if (speaker.hasLink && activity != null) {
                navigation_speaker_link.setImageResource(speaker.imageLinkResourceId)
                navigation_speaker_link.setOnClickListener {
                    activity!!.startActivity(Intent(Intent.ACTION_VIEW, speaker.linkUri))
                }
            }
            (speakerDetailTalkList.adapter as TalkListAdapter).update(speaker.talks)
        })

        model.loadSpeaker(arguments?.getString(MiXiTApplication.OBJECT_ID) ?: "")
    }

    override fun onStop() {
        super.onStop()
        navigation_speaker_link.setOnClickListener(null)
    }
}


