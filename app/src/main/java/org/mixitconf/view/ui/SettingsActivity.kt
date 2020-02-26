package org.mixitconf.view.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import org.mixitconf.MiXiTApplication
import org.mixitconf.R
import org.mixitconf.mixitApp

class SettingsActivity : MixitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager.beginTransaction().replace(R.id.settings, SettingsFragment()).commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onPause() {
            PreferenceManager.getDefaultSharedPreferences(mixitApp).apply {
                val userWantsAutomaticUpdate = this.getBoolean(MiXiTApplication.PREF_DATA_SYNC, true)
                if (userWantsAutomaticUpdate) {
                    MiXiTApplication.scheduleAutomaticDataUpdate(mixitApp)
                }
                else{
                    MiXiTApplication.cancelDataUpdate(mixitApp)
                }
            }
            super.onPause()
        }
    }

}