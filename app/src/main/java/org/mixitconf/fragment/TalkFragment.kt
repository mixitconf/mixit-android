package org.mixitconf.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_datalist.*
import org.mixitconf.OnTalkSelectedListener
import org.mixitconf.R
import org.mixitconf.adapter.TalkListAdapter
import org.mixitconf.model.Talk
import org.mixitconf.model.TalkFormat
import org.mixitconf.repository.TalkReader
import org.mixitconf.service.endLocale
import org.mixitconf.service.mixitApp
import org.mixitconf.service.startLocale


class TalkFragment : Fragment() {

    private var listState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_datalist, container, false)

    override fun onPause() {
        super.onPause()
        listState = (dataList.layoutManager as LinearLayoutManager).onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()
        val talks = mixitApp.talkReader
                .findAll()
                .filter { it.format != TalkFormat.RANDOM }
                .toMutableList()

        talks.addAll(mixitApp.talkReader.findMarkers())

        // Lookup the recycler view in activity layout
        dataList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).also {
                it.onRestoreInstanceState(listState)
            }
            adapter = TalkListAdapter(activity as OnTalkSelectedListener, resources).also {
                it.update(talks.sortedWith(compareBy<Talk> { it.startLocale() }.thenBy { it.endLocale() }.thenBy { it.room }))
            }
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        }
    }
}
