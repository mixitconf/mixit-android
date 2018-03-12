package org.mixitconf

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_speakers.*
import org.mixitconf.adapter.UserListAdapter
import org.mixitconf.service.SpeakerService

class SpeakerActivity : AbstractMixitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speakers)

        val speakers = SpeakerService.getInstance(this).findSpeakers()

        // Lookup the recyclerview in activity layout
        speakerList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = UserListAdapter(speakers, context)
        }
    }
}
