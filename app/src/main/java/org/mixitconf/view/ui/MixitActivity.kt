package org.mixitconf.view.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.mixitconf.R
import org.mixitconf.hasIntentPackage
import org.mixitconf.mixitApp
import org.mixitconf.service.synchronization.SynchronizationService

open class MixitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the top action bar to display Home button
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
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

    /**
     * Listener used on top action bar when a user clicks on an action
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
            R.id.navigation_github -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse("https://github.com/mixitconf")
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            R.id.navigation_synchronize -> {
                if (mixitApp.hasPermission(Manifest.permission.INTERNET)) {
                    Intent(applicationContext, SynchronizationService::class.java).also { intent ->
                        startService(intent)
                    }
                } else {
                    Toast.makeText(applicationContext, R.string.networkPermission, Toast.LENGTH_LONG).show()
                }
            }
            R.id.navigation_twitter -> {
                val hasTwitterApp = applicationContext.hasIntentPackage("com.twitter.android")
                val intentUri = if (hasTwitterApp) "twitter://user?screen_name=mixitconf" else "https://twitter.com/mixitconf"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(intentUri)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.comeToPartyButton -> {
                if (applicationContext.hasIntentPackage("com.google.android.apps.maps")) {
                    val intent = Intent(
                        Intent.ACTION_VIEW, Uri.parse("geo:5.7366857,4.8128151?z=18&q=Le+Sucre,+50+Quai+Rambaud,+69002+Lyon")
                    )
                    startActivity(intent)
                }
            }
            R.id.comeToMiXiTButton -> {
                if (applicationContext.hasIntentPackage("com.google.android.apps.maps")) {
                    val intent = Intent(
                        Intent.ACTION_VIEW, Uri.parse("geo:45.7481118,4.8591068?z=18&q=Manufacture+des+Tabacs,6+rue+professeur+Rollet,+69008+Lyon")
                    )
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
