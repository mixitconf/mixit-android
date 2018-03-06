package org.mixitconf.mixitconf

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.mixitconf.mixitconf.fragment.HomeFragment
import org.mixitconf.mixitconf.fragment.SpeakerFragment
import org.mixitconf.mixitconf.fragment.TalkFragment

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        when (item.itemId) {
            R.id.navigation_home -> {
                fragmentTransaction.replace(R.id.homeFragment, HomeFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_talks -> {
                fragmentTransaction.replace(R.id.homeFragment, TalkFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_speakers -> {
                fragmentTransaction.replace(R.id.homeFragment, SpeakerFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    /**
     * Override the title to display only MiXiT logo
     */
    override fun onTitleChanged(title: CharSequence, color: Int) {
        super.onTitleChanged(title, color)
        delegate.setTitle("")
    }
}