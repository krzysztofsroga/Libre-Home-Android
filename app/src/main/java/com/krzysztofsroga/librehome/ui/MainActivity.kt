package com.krzysztofsroga.librehome.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.krzysztofsroga.librehome.AppConfig
import com.krzysztofsroga.librehome.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val listener = object : ViewPager.OnPageChangeListener {
        var jumpPosition = -1

        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager.SCROLL_STATE_IDLE && jumpPosition >= 0) {
                pager.setCurrentItem(jumpPosition, false)
                jumpPosition = -1
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            val goodPosition = (position + positionOffset - 1 + adapter.realCount).roundToInt().rem(adapter.realCount)
            // position + positionoffset gives value + 1. So -1 is added. Then adapter.realcount is added because of lack of modulo function(only reminder)
            title = adapter.getTitle(goodPosition)
        }

        override fun onPageSelected(position: Int) {
            when (position) {
                0 -> jumpPosition = adapter.realCount
                adapter.realCount + 1 -> jumpPosition = 1
            }
        }

        private val adapter: ScreenSlidePagerAdapter
            get() = (pager.adapter as ScreenSlidePagerAdapter)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        pager.adapter = ScreenSlidePagerAdapter(
            supportFragmentManager,
            AppConfig.screens
        )
        pager.addOnPageChangeListener(listener)
        pager.currentItem =
            AppConfig.defaultScreenIndex

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.action_about -> {

            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


}
