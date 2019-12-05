package com.krzysztofsroga.librehome

import com.krzysztofsroga.librehome.ui.MainFragment
import com.krzysztofsroga.librehome.ui.music.MusicFragment
import com.krzysztofsroga.librehome.ui.switches.SwitchesFragment

object AppConfig {

    const val defaultScreenIndex = 1
    val screens: List<MainActivityFragmentFactory<*>> = listOf(
        MainFragment,
//        MusicFragment,
        SwitchesFragment
    )
}