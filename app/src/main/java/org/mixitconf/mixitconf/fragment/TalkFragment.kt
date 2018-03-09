package org.mixitconf.mixitconf.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mixitconf.mixitconf.R
import org.mixitconf.mixitconf.adapter.OnClickListener
import org.mixitconf.mixitconf.adapter.TalkListAdapter
import org.mixitconf.mixitconf.model.Talk
import org.mixitconf.mixitconf.model.TalkFormat
import org.mixitconf.mixitconf.repository.TalkReader


class TalkFragment : Fragment() {

    var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_talks, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (recyclerView != null && recyclerView!!.getAdapter() == null) {
            val talks = TalkReader.getInstance(context)
                    .findAll()
                    .filter { it.format != TalkFormat.RANDOM }
                    .sortedBy { it.room }
                    .sortedBy { it.start }
            val adapter = TalkListAdapter(TalkOnClickListener(), talks)
            recyclerView!!.adapter = adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.talk_list)
        recyclerView?.setLayoutManager(LinearLayoutManager(context))
        recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    class TalkOnClickListener() : OnClickListener<Talk> {
        override fun invoke(p1: Talk) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}
