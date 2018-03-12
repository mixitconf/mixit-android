package org.mixitconf

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_talks.*
import org.mixitconf.adapter.TalkListAdapter
import org.mixitconf.model.Talk
import org.mixitconf.model.TalkFormat
import org.mixitconf.repository.TalkReader


class TalkActivity : AbstractMixitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talks)

        val talks = TalkReader.getInstance(baseContext)
                .findAll()
                .filter { it.format != TalkFormat.RANDOM }
                .toMutableList()

        talks.addAll(TalkReader.getInstance(baseContext).findMarkers())

        // Lookup the recyclerview in activity layout
        talkList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = TalkListAdapter(talks.sortedWith(compareBy<Talk> { it.start }.thenBy { it.end }.thenBy { it.room }), context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}
