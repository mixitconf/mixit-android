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
import org.mixitconf.mixitconf.adapter.UserListAdapter
import org.mixitconf.mixitconf.model.User
import org.mixitconf.mixitconf.service.SpeakerService


class SpeakerFragment : Fragment() {

    var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_speakers, container, false)
    }

    override fun onResume(){
        super.onResume()
        if (recyclerView!=null && recyclerView!!.getAdapter() == null) {
            val speakers = SpeakerService.getInstance(context).findSpeakers()
            val adapter = UserListAdapter(UserOnClickListener(), speakers)
            recyclerView!!.adapter = adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.speaker_list)
        recyclerView?.setLayoutManager(LinearLayoutManager(context))
        recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    class UserOnClickListener(): OnClickListener<User>{
        override fun invoke(p1: User) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}
