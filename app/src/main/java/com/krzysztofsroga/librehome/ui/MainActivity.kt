package com.krzysztofsroga.librehome.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.krzysztofsroga.librehome.AppConfig
import com.krzysztofsroga.librehome.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    var jumpPosition = -1

    private val adapter: ScreenSlidePagerAdapter
        get() = (pager.adapter as ScreenSlidePagerAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        pager.adapter = ScreenSlidePagerAdapter(
            supportFragmentManager,
            AppConfig.screens
        )
        pager.addOnPageChangeListener(this)
        pager.currentItem = AppConfig.defaultScreenIndex
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_about -> {
                startActivity(Intent(this, BottomNavigationActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

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
}
