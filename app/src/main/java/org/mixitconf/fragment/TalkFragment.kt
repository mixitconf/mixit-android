package org.mixitconf.fragment

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_datalist.*
import org.mixitconf.R
import org.mixitconf.adapter.TalkListAdapter
import org.mixitconf.model.Talk
import org.mixitconf.model.TalkFormat
import org.mixitconf.repository.TalkReader


class TalkFragment : Fragment() {

    private var listState: Parcelable? = null

    /**
     * Interface implemented by parent activity to display a talk when user clicks on a talk in the list
     */
    interface OnTalkSelectedListener {
        fun onTalkSelected(id: String):Int
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_datalist, container, false)

    override fun onPause() {
        super.onPause()
        listState = (dataList.layoutManager as LinearLayoutManager).onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()
        val talks = TalkReader.getInstance(context!!)
                .findAll()
                .filter { it.format != TalkFormat.RANDOM }
                .toMutableList()

        talks.addAll(TalkReader.getInstance(context!!).findMarkers())

        // Lookup the recycler view in activity layout
        dataList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = TalkListAdapter(talks.sortedWith(compareBy<Talk> { it.start }.thenBy { it.end }.thenBy { it.room }), context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager.onRestoreInstanceState(listState)
        }
    }
}
