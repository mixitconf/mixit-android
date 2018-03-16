package org.mixitconf

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.mixitconf.fragment.*
import org.mixitconf.service.hasIntentPackage
import org.mixitconf.service.withIdInBundle

open class MainActivity : AppCompatActivity(),
        TalkFragment.OnTalkSelectedListener,
        SpeakerFragment.OnSpeakerSelectedListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the top ction bar to display Home button
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    /**
     * Initializes top action bar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.global, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Override the title to display only MiXiT logo
     */
    override fun onTitleChanged(title: CharSequence, color: Int) {
        super.onTitleChanged(title, color)
        delegate.setTitle("")
    }

    override fun onTalkSelected(id: String) {
        supportFragmentManager.beginTransaction().apply {
            val fragment = TalkDetailFragment().withIdInBundle(id)
            addToBackStack(fragment.tag)
            replace(R.id.container, fragment).commit()
        }
    }

    override fun onSpeakerSelected(id: String) {
        supportFragmentManager.beginTransaction().apply {
            val fragment = SpeakerDetailFragment().withIdInBundle(id)
            addToBackStack(fragment.tag)
            replace(R.id.container, fragment).commit()
        }
    }

    /**
     * Listener used on top action bar when a user clicks on an action
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, MainActivity::class.java))
                return true
            }
            R.id.navigation_about -> {
                DialogAboutFragment().show(fragmentManager, resources.getString(R.string.about_title))
                return true
            }
            R.id.navigation_github -> {
                baseContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mixitconf")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                return true
            }
            R.id.navigation_twitter -> {
                val hasTwitterApp = baseContext.hasIntentPackage("com.twitter.android")
                val intentUri = if (hasTwitterApp) "twitter://user?screen_name=mixitconf" else "https://twitter.com/mixitconf"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(intentUri)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                baseContext.startActivity(intent)
                return true
            }
            R.id.comeToPartyButton -> {
                val hasMapApp = baseContext.hasIntentPackage("com.google.android.apps.maps")
                if (hasMapApp) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:45.767643,4.8328633?z=17&q=Hôtel+de+Ville+de+Lyon"))
                    baseContext.startActivity(intent)
                }
                return true
            }
            R.id.comeToMiXiTButton -> {
                val hasMapApp = baseContext.hasIntentPackage("com.google.android.apps.maps")
                if (hasMapApp) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:45.78392,4.869014?z=17&q=CPE+Lyon,+43+Boulevard+du+11+novembre,+69100+Villeurbanne"))
                    baseContext.startActivity(intent)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Listener used on bottom action bar when a user clicks on an action
     */
    val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_talk -> {
                supportFragmentManager.beginTransaction().apply {
                    val fragment = TalkFragment()
                    addToBackStack(fragment.tag)
                    replace(R.id.container, fragment).commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_speaker -> {
                supportFragmentManager.beginTransaction().apply {
                    val fragment = SpeakerFragment()
                    addToBackStack(fragment.tag)
                    replace(R.id.container, fragment).commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_twitter -> {
                val hasTwitterApp = baseContext.hasIntentPackage("com.twitter.android")
                val intentUri = if (hasTwitterApp) "twitter://user?screen_name=mixitconf" else "https://twitter.com/mixitconf"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(intentUri)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                baseContext.startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_website -> {
                baseContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://mixitconf.org/schedule")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

}
