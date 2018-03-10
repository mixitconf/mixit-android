package org.mixitconf.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mixitconf.R
import org.mixitconf.adapter.OnClickListener
import org.mixitconf.adapter.TalkListAdapter
import org.mixitconf.model.Talk
import org.mixitconf.model.TalkFormat
import org.mixitconf.repository.TalkReader


class TalkFragment : Fragment() {

    var recyclerView: RecyclerView? = null

    companion object {
        fun newInstance(): TalkFragment = TalkFragment()
    }


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
            val adapter = TalkListAdapter(TalkOnClickListener(activity as AppCompatActivity), talks)
            recyclerView!!.adapter = adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.talk_list)
        recyclerView?.setLayoutManager(LinearLayoutManager(context))
        recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    class TalkOnClickListener(val parent: AppCompatActivity) : OnClickListener<Talk> {
        override fun invoke(talk: Talk) {
            parent.supportFragmentManager.beginTransaction().replace(R.id.homeFragment, TalkDetailFragment.newInstance(talk)).commit()
        }

    }
}
