package org.mixitconf

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.mixitconf.fragment.*
import org.mixitconf.service.hasIntentPackage
import org.mixitconf.service.openFragment
import org.mixitconf.service.openFragmentDetail

/**
 * Interface implemented by parent activity to display a talk when user clicks on a talk in the list
 */
interface OnTalkSelectedListener {
    fun onTalkSelected(id: String):Int
}

open class MainActivity : AppCompatActivity(), OnTalkSelectedListener, SpeakerFragment.OnSpeakerSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the top action bar to display Home button
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

    override fun onTalkSelected(id: String) = openFragmentDetail(id, TalkDetailFragment())

    override fun onSpeakerSelected(id: String) = openFragmentDetail(id, SpeakerDetailFragment())

    /**
     * Listener used on top action bar when a user clicks on an action
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.navigation_about -> {
                DialogAboutFragment().show(fragmentManager, resources.getString(R.string.about_title))
            }
            R.id.navigation_github -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mixitconf")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
            R.id.navigation_twitter -> {
                val hasTwitterApp = applicationContext.hasIntentPackage("com.twitter.android")
                val intentUri = if (hasTwitterApp) "twitter://user?screen_name=mixitconf" else "https://twitter.com/mixitconf"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(intentUri)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.comeToPartyButton -> {
                if(applicationContext.hasIntentPackage("com.google.android.apps.maps")){
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:5.7366857,4.8128151?z=18&q=Le+Sucre,+50+Quai+Rambaud,+69002+Lyon"))
                    startActivity(intent)
                }
            }
            R.id.comeToMiXiTButton -> {
                if(applicationContext.hasIntentPackage("com.google.android.apps.maps")){
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:45.7481118,4.8591068?z=18&q=Manufacture+des+Tabacs,6+rue+professeur+Rollet,+69008+Lyon"))
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Listener used on bottom action bar when a user clicks on an action
     */
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_talk -> {
                openFragment(TalkFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_speaker -> {
                openFragment(SpeakerFragment())
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
