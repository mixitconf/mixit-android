package org.mixitconf.view.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_datalist.*
import org.mixitconf.R
import org.mixitconf.default
import org.mixitconf.view.adapter.SpeakerListAdapter
import org.mixitconf.view.adapter.TalkListAdapter
import org.mixitconf.view.ui.OnSpeakerSelectedListener
import org.mixitconf.viewmodel.SpeakerListViewModel
import org.mixitconf.viewmodel.TalkListViewModel
import java.util.*


class SpeakerFragment : Fragment() {

    private var listState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_datalist, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dataList.default { SpeakerListAdapter(activity as OnSpeakerSelectedListener) }.apply { layoutManager?.onRestoreInstanceState(listState) }
    }

    override fun onResume() {
        super.onResume()
        ViewModelProvider(this).get(SpeakerListViewModel::class.java).liveData.observe(viewLifecycleOwner, Observer {
            (dataList.adapter as SpeakerListAdapter).update(it)
        })
    }

    override fun onPause() {
        super.onPause()
        listState = (dataList.layoutManager as LinearLayoutManager).onSaveInstanceState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ViewModelProvider(this).get(SpeakerListViewModel::class.java).liveData.removeObservers(viewLifecycleOwner)
    }
}
