package com.krzysztofsroga.librehome.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.krzysztofsroga.librehome.AppConfig
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.utils.Logger
import com.krzysztofsroga.librehome.utils.stackTraceString
import com.krzysztofsroga.librehome.viewmodels.SwitchesViewModel
import kotlinx.android.synthetic.main.activity_bottom_navigation.*


class BottomNavigationActivity : AppCompatActivity() {

    private val switchesViewModel: SwitchesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration( // TODO just read bottom_nav_menu.xml?
            setOf(
                R.id.navigation_home,
                R.id.navigation_sensors,
                R.id.navigation_groups,
                R.id.navigation_switches
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

        swipe_refresh.setOnRefreshListener {
            refreshSwitches()
        }

        switchesViewModel.switches.observe(this, Observer {
            swipe_refresh.isRefreshing = false //TODO refreshing scenes also should end refreshing
        })

        switchesViewModel.error.observe(this, Observer { e ->
            val error = e.value ?: return@Observer
            swipe_refresh.isRefreshing = false
            Logger.e("Domoticz Connection", error.toString() + error.stackTraceString)
            error.printStackTrace()
            Toast.makeText(this, getString(R.string.conn_err) + error.toString(), Toast.LENGTH_LONG).show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConfig.RequestCodes.settings) {
            reload()
        }
    }

    private fun reload() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun refreshSwitches() {
        swipe_refresh.isRefreshing = true
        switchesViewModel.updateSwitches()
        switchesViewModel.updateScenes()
    }

    override fun onStart() {
        super.onStart()
        refreshSwitches()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivityForResult(Intent(this, SettingsActivity::class.java), AppConfig.RequestCodes.settings)
                true
            }
            R.id.action_about -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.about_url))))
                true
            }
            R.id.action_refresh -> {
                refreshSwitches()
                true
            }
            R.id.action_logs -> {
                startActivity(Intent(this, LogsActvity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
