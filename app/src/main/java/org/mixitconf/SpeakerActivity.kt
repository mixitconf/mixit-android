package org.mixitconf

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_speakers.*
import org.mixitconf.adapter.OnClickListener
import org.mixitconf.adapter.UserListAdapter
import org.mixitconf.model.User
import org.mixitconf.service.SpeakerService

class SpeakerActivity : AbstractMixitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speakers)
        speakersNavigation.setOnNavigationItemSelectedListener(getNavigationItemSelectedListener())

        val speakers = SpeakerService.getInstance(this).findSpeakers()
        val dataAdapter = UserListAdapter(UserOnClickListener(this), speakers, this)

        // Lookup the recyclerview in activity layout
        speakerList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = dataAdapter
        }
    }

    class UserOnClickListener(val parent: AppCompatActivity) : OnClickListener<User> {
        override fun invoke(user: User) {
            //parent.supportFragmentManager.beginTransaction().replace(R.id.homeFragment, TalkDetailFragment.newInstance(talk)).commit()
        }

    }

}
