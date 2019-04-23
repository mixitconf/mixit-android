package org.mixitconf.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_speaker_detail.*
import kotlinx.android.synthetic.main.fragment_speaker_detail_content.*
import org.mixitconf.OnTalkSelectedListener
import org.mixitconf.R
import org.mixitconf.adapter.TalkListAdapter
import org.mixitconf.mixitApp
import org.mixitconf.model.entity.*
import org.mixitconf.service.Constant
import org.mixitconf.service.default
import org.mixitconf.service.visibility
import org.mixitconf.vm.SpeakerDetailViewModel


class SpeakerDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_speaker_detail, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        speakerDetailTalkList.default { TalkListAdapter(activity as OnTalkSelectedListener, mixitApp.resources)}

        val model = ViewModelProviders.of(this).get(SpeakerDetailViewModel::class.java)

        model.liveData.observe(this, Observer { speaker ->
            speakerDetailName.text = speaker.fullname
            speakerDetailCompany.text = speaker.company
            speakerDetailDescription.text = speaker.descriptionInMarkdown
            speakerDetailImage.setSpeakerImage(speaker)

            if (speaker.hasLink) {
                navigation_speaker_link.setImageResource(speaker.imageLinkResourceId)
                navigation_speaker_link.setOnClickListener {
                    mixitApp.applicationContext.startActivity(Intent(Intent.ACTION_VIEW, speaker.linkUri))
                }
            }
            navigation_speaker_link.visibility = speaker.hasLink.visibility
            (speakerDetailTalkList.adapter as TalkListAdapter).update(speaker.talks)
        })

        model.loadSpeaker(arguments?.getString(Constant.OBJECT_ID) ?: "")
    }

    override fun onStop() {
        super.onStop()
        navigation_speaker_link.setOnClickListener(null)
    }
}


