package org.mixitconf

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.mixitconf.service.hasIntentPackage

open class MainActivity : AbstractMixitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        mapCpeFloor0Button.setOnClickListener(onButtonFloorOneMapClickListener)
        mapCpeSubFloorButton.setOnClickListener(onButtonSubFloorMapClickListener)
        comeToPartyButton.setOnClickListener(onButtonComeToPartyClickListener)
        comeToMiXiTButton.setOnClickListener(onButtonComeToMixitClickListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_twitter -> {
                val hasTwitterApp = baseContext.hasIntentPackage("com.twitter.android")
                val intentUri = if (hasTwitterApp) "twitter://user?screen_name=mixitconf" else "https://twitter.com/mixitconf"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(intentUri)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                baseContext.startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_website -> {
                baseContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://mixitconf.org")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_github -> {
                baseContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mixitconf")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_mail -> {
                baseContext.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:contact@mix-it.fr")))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val onButtonComeToMixitClickListener = View.OnClickListener { _ ->
        run {
            val hasMapApp = baseContext.hasIntentPackage("com.google.android.apps.maps")
            if (hasMapApp) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:45.78392,4.869014?z=17&q=CPE+Lyon,+43+Boulevard+du+11+novembre,+69100+Villeurbanne"))
                baseContext.startActivity(intent)
            }
        }
    }

    private val onButtonComeToPartyClickListener = View.OnClickListener { _ ->
        run {
            val hasMapApp = baseContext.hasIntentPackage("com.google.android.apps.maps")
            if (hasMapApp) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:45.767643,4.8328633?z=17&q=Hôtel+de+Ville+de+Lyon"))
                baseContext.startActivity(intent)
            }
        }
    }

    private val onButtonSubFloorMapClickListener = View.OnClickListener { _ ->
        run {
            val intent = Intent(baseContext, FloorMapActivity::class.java).apply {
                putExtra(FloorMapActivity.FLOOR_ID, -1)
            }
            baseContext.startActivity(intent)
        }
    }

    private val onButtonFloorOneMapClickListener = View.OnClickListener { _ ->
        run {
            val intent = Intent(baseContext, FloorMapActivity::class.java).apply {
                putExtra(FloorMapActivity.FLOOR_ID, 1)
            }
            baseContext.startActivity(intent)
        }
    }

}
