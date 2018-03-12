package org.mixitconf

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import org.mixitconf.fragment.DialogAboutFragment

abstract class AbstractMixitActivity() : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.global, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_about -> {
                val dial = DialogAboutFragment()
                dial.show(fragmentManager, resources.getString(R.string.about_title))
                return true
            }
            R.id.navigation_talks -> {
                val intent = Intent(this, TalkActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.navigation_speakers -> {
                val intent = Intent(this, SpeakerActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Override the title to display only MiXiT logo
     */
    override fun onTitleChanged(title: CharSequence, color: Int) {
        super.onTitleChanged(title, color)
        delegate.setTitle("")
    }


}
