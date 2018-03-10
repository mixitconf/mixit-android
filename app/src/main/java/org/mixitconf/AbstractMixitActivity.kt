package org.mixitconf

import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity

abstract class AbstractMixitActivity : AppCompatActivity() {

    fun getNavigationItemSelectedListener() = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        //val fragmentTransaction = supportFragmentManager.beginTransaction()
        when (item.itemId) {
            R.id.navigation_home -> {
                //fragmentTransaction.replace(R.id.homeFragment, HomeFragment.newInstance()).commit()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_talks -> {
                //fragmentTransaction.replace(R.id.homeFragment, TalkFragment.newInstance()).commit()
                val intent = Intent(this, TalkActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_speakers -> {
                //fragmentTransaction.replace(R.id.homeFragment, SpeakerFragment.newInstance()).commit()
                val intent = Intent(this, SpeakerActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    /**
     * Override the title to display only MiXiT logo
     */
    override fun onTitleChanged(title: CharSequence, color: Int) {
        super.onTitleChanged(title, color)
        delegate.setTitle("")
    }


}
