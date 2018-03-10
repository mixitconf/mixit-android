package org.mixitconf

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.mixitconf.adapter.OnClickListener
import org.mixitconf.adapter.UserListAdapter
import org.mixitconf.model.User
import org.mixitconf.service.SpeakerService

class SpeakerActivity : AbstractMixitActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speakers)
        navigation.setOnNavigationItemSelectedListener(getNavigationItemSelectedListener())

        val speakers = SpeakerService.getInstance(this).findSpeakers()
        viewManager = LinearLayoutManager(this)
        viewAdapter = UserListAdapter(UserOnClickListener(this), speakers, this)

        // Lookup the recyclerview in activity layout
        recyclerView = findViewById<RecyclerView>(R.id.speaker_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    class UserOnClickListener(val parent: AppCompatActivity) : OnClickListener<User> {
        override fun invoke(user: User) {
            //parent.supportFragmentManager.beginTransaction().replace(R.id.homeFragment, TalkDetailFragment.newInstance(talk)).commit()
        }

    }

}
