package org.mixitconf.view.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_datalist.*
import org.mixitconf.R
import org.mixitconf.default
import org.mixitconf.mixitApp
import org.mixitconf.model.entity.Talk
import org.mixitconf.view.adapter.TalkListAdapter
import org.mixitconf.view.ui.OnTalkSelectedListener
import org.mixitconf.viewmodel.TalkListViewModel
import java.util.*


class TalkFragment(val displayOnlyFavorites: Boolean = false) : Fragment() {

    private var listState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_datalist, container, false)

    override fun onPause() {
        super.onPause()
        listState = (dataList.layoutManager as LinearLayoutManager).onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()
        ViewModelProvider(this).get(TalkListViewModel::class.java).apply {
            load()
            liveData.observe(viewLifecycleOwner, Observer {
                getTalkToDiplay(it).apply {
                    (dataList.adapter as TalkListAdapter).update(this)
                    if (listState == null) {
                        val index = this.indexOfFirst { talk -> Date().let { talk.start.time > it.time } }
                        dataList.scrollToPosition(index)
                    }
                }
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dataList
            .default { TalkListAdapter(activity as OnTalkSelectedListener, resources) }
            .apply { layoutManager?.onRestoreInstanceState(listState) }
    }

    private fun getTalkToDiplay(talks: List<Talk>): List<Talk> = talks
        .filter { !displayOnlyFavorites || it.favorite }
        .apply {
            if (isEmpty()) {
                Toast.makeText(mixitApp, R.string.talk_no_favorite, Toast.LENGTH_LONG).show()
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        ViewModelProvider(this).get(TalkListViewModel::class.java).liveData.removeObservers(viewLifecycleOwner)
    }
}
