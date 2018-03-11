package org.mixitconf

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_talks.*
import org.mixitconf.adapter.TalkListAdapter
import org.mixitconf.model.TalkFormat
import org.mixitconf.repository.TalkReader


class TalkActivity : AbstractMixitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talks)
        talksNavigation.setOnNavigationItemSelectedListener(getNavigationItemSelectedListener())

        val talks = TalkReader.getInstance(baseContext)
                .findAll()
                .filter { it.format != TalkFormat.RANDOM }
                .sortedBy { it.room }
                .sortedBy { it.start }


        // Lookup the recyclerview in activity layout
        talkList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = TalkListAdapter(talks, context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}
