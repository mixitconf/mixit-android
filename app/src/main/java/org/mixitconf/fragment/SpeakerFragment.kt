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
import org.mixitconf.R
import org.mixitconf.adapter.UserListAdapter
import org.mixitconf.service.mixitApp


class SpeakerFragment : Fragment() {

    private var listState:Parcelable? = null

    /**
     * Interface implemented by parent activity to display a speaker when user clicks on a speaker in the list
     */
    interface OnSpeakerSelectedListener {
        fun onSpeakerSelected(id: String):Int
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_datalist, container, false)

    override fun onPause() {
        super.onPause()
        listState = (dataList.layoutManager as LinearLayoutManager).onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()

        val speakers = mixitApp.speakerService.findSpeakers().sortedBy { it.firstname }

        // Lookup the recycler view in activity layout
        dataList.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context).also {
                it.onRestoreInstanceState(listState)
            }
            adapter = UserListAdapter(activity as OnSpeakerSelectedListener).also {
                it.update(speakers)
            }


        }
    }
}
