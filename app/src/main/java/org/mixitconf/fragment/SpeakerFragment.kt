package org.mixitconf.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_datalist.*
import org.mixitconf.R
import org.mixitconf.adapter.UserListAdapter
import org.mixitconf.service.SpeakerService


class SpeakerFragment : Fragment() {

    /**
     * Interface implemented by parent activity to display a speaker when user clicks on a speaker in the list
     */
    interface OnSpeakerSelectedListener {
        fun onSpeakerSelected(id: String):Int
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_datalist, container, false)
    }

    override fun onResume() {
        super.onResume()

        val speakers = SpeakerService.getInstance(context!!).findSpeakers().sortedBy { it.firstname }

        // Lookup the recycler view in activity layout
        dataList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = UserListAdapter(speakers, context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}
