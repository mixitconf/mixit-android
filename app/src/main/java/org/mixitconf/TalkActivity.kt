package org.mixitconf

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*
import org.mixitconf.adapter.TalkListAdapter
import org.mixitconf.model.Talk
import org.mixitconf.model.TalkFormat
import org.mixitconf.repository.TalkReader


class TalkActivity : AbstractMixitActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talks)
        navigation.setOnNavigationItemSelectedListener(getNavigationItemSelectedListener())

        val talks = TalkReader.getInstance(baseContext)
                .findAll()
                .filter { it.format != TalkFormat.RANDOM }
                .sortedBy { it.room }
                .sortedBy { it.start }

        viewManager = LinearLayoutManager(this)
        viewAdapter = TalkListAdapter(talks, this)

        // Lookup the recyclerview in activity layout
        recyclerView = findViewById<RecyclerView>(R.id.talk_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            addOnItemTouchListener(OnTalkClickListener(context, talks))
        }
    }

    inner class OnTalkClickListener(val context: Context, val talks:List<Talk>): RecyclerView.SimpleOnItemTouchListener(){
        override fun onTouchEvent(view: RecyclerView, e: MotionEvent) {
            val childView = view.findChildViewUnder(e.x, e.y)
            val position = view.getChildAdapterPosition(childView)
            val talk = talks.get(position)

            //if(!talk.dummy) {
                val intent = Intent(context, TalkDetailActivity::class.java).apply {
                    putExtra(TalkDetailActivity.TALK_ID, talk.id)
                }
                context.startActivity(intent)
            //}
        }
    }
}
