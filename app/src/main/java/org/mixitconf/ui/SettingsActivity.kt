package org.mixitconf.ui

import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import org.mixitconf.R
import org.mixitconf.booleanSharedPrefs
import org.mixitconf.mixitApp
import org.mixitconf.model.enums.SettingValue
import org.mixitconf.service.Workers

class SettingsActivity : MixitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager.beginTransaction().replace(R.id.settings, SettingsFragment()).commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.mixit_preferences, rootKey)
        }

        override fun onStart() {
            super.onStart()
            preferenceManager.findPreference<EditTextPreference>(SettingValue.EMAIL_SYNC.key)?.apply {
                this.setOnBindEditTextListener { it.inputType =  InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS }
            }
            preferenceManager.findPreference<EditTextPreference>(SettingValue.FAVORITE_NOTIFICATION_DURATION.key)?.apply {
                this.setOnBindEditTextListener { it.inputType =   InputType.TYPE_CLASS_NUMBER }
            }
        }

        override fun onPause() {
            Workers.cancelSynchronizationPeriodicWorker(mixitApp)
            Workers.cancelFavoritePeriodicWorker(mixitApp)

            if (mixitApp.booleanSharedPrefs(SettingValue.DATA_AUTO_SYNC_ENABLE)) {
                Workers.createSpeakerSynchronizationWorker(mixitApp)
            }

            if (mixitApp.booleanSharedPrefs(SettingValue.FAVORITE_NOTIFICATION_ENABLE)) {
                Workers.createFavoritePeriodicWorker(mixitApp)
            }
            super.onPause()
        }
    }

}