package org.mixitconf.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.mixitconf.MiXiTApplication
import org.mixitconf.R
import org.mixitconf.openFragment
import org.mixitconf.openFragmentDetail
import org.mixitconf.ui.speaker.detail.SpeakerDetailFragment
import org.mixitconf.ui.speaker.list.SpeakerListFragment
import org.mixitconf.ui.talk.detail.TalkDetailFragment
import org.mixitconf.ui.talk.list.TalkListFragment

/**
 * Interface implemented by parent activity to display a talk when user clicks on a talk in the list
 */
interface OnTalkSelectedListener {
    fun onTalkSelected(id: String): Int
}

/**
 * Interface implemented by parent activity to display a speaker when user clicks on a speaker in the list
 */
interface OnSpeakerSelectedListener {
    fun onSpeakerSelected(id: String): Int
}

open class MainActivity : MixitActivity(), OnTalkSelectedListener, OnSpeakerSelectedListener {

    val talkFragment by lazy {
        TalkListFragment()
    }
    val favoriteFragment by lazy {
        TalkListFragment(displayOnlyFavorites = true)
    }
    val speakerFragment by lazy {
        SpeakerListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // When activity is started via a notification we want to open a specific fragment
        val fragmentId = intent.getIntExtra(MiXiTApplication.FRAGMENT_ID, 0)
        when (fragmentId) {
            R.id.navigation_talk -> openFragment(talkFragment)
            R.id.navigation_speaker -> openFragment(speakerFragment)
            else -> {
            }
        }
    }

    override fun onTalkSelected(id: String) = openFragmentDetail(id, TalkDetailFragment())

    override fun onSpeakerSelected(id: String) = openFragmentDetail(id, SpeakerDetailFragment())

    /**
     * Listener used on bottom action bar when a user clicks on an action
     */
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_talk -> {
                openFragment(talkFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_speaker -> {
                openFragment(speakerFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                openFragment(favoriteFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

}
