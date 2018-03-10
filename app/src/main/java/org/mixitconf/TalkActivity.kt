package org.mixitconf

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.mixitconf.adapter.OnClickListener
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
        viewAdapter = TalkListAdapter(TalkOnClickListener(this), talks, this)

        // Lookup the recyclerview in activity layout
        recyclerView = findViewById<RecyclerView>(R.id.talk_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    class TalkOnClickListener(val parent: AppCompatActivity) : OnClickListener<Talk> {
        override fun invoke(talk: Talk) {
            //parent.supportFragmentManager.beginTransaction().replace(R.id.homeFragment, TalkDetailFragment.newInstance(talk)).commit()
        }

    }
}
