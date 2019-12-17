package com.krzysztofsroga.librehome.ui

import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.krzysztofsroga.librehome.R
import com.yariksoffice.lingver.Lingver
import java.util.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.settings,
                SettingsFragment()
            )
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            preferenceManager.findPreference<EditTextPreference>("music_update")?.apply {
                setOnBindEditTextListener { editText ->
                    editText.inputType = InputType.TYPE_CLASS_NUMBER
                }
                setOnPreferenceChangeListener { _, newValue ->
                    newValue is String && newValue.toInt() in 1..60 //TODO display error if not in range
                }
            }

            preferenceManager.findPreference<ListPreference>("language")?.apply {
                setOnPreferenceChangeListener { _, newValue ->
                    Lingver.getInstance().setLocale(requireActivity().applicationContext, Locale(newValue as String))
                    true
                }
            }
        }
    }
}