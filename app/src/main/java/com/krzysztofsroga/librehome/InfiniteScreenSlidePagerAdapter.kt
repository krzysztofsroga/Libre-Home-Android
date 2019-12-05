package com.krzysztofsroga.librehome

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ScreenSlidePagerAdapter(fm: FragmentManager, private val fragmentList: List<MainActivityFragmentFactory<*>>) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        val listPosition = positionToListPosition(position)
        return fragmentList[listPosition].newInstance()
    }

    private fun positionToListPosition(pagerPosition: Int): Int {
        return when (pagerPosition) {
            0 -> realCount - 1
            realCount + 1 -> 0
            else -> pagerPosition - 1
        }
    }

    override fun getCount(): Int {
        return if (realCount > 0) realCount + 2 else 0
    }

    val realCount
        get() = fragmentList.size

    fun getTitle(position: Int): String {
        return fragmentList[position].name
    }


}