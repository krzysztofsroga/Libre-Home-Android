package com.krzysztofsroga.librehome

import com.krzysztofsroga.librehome.ui.MainActivityFragmentFactory
import com.krzysztofsroga.librehome.ui.MainFragment
import com.krzysztofsroga.librehome.ui.mylists.SwitchGroupFragment
import com.krzysztofsroga.librehome.ui.switches.SwitchesFragment

object AppConfig {

    const val defaultScreenIndex = 1
    val screens: List<MainActivityFragmentFactory<*>> = listOf(
        MainFragment,
        SwitchesFragment,
        SwitchGroupFragment
    )

    const val sshPort = 22
    const val rPiUsername = "pi"
    const val defaultDomoticzHostname = "domoticz"  //"192.168.2.191"

    object PrefKeys {
        const val HOST = "host"
        const val SSH_PASSWORD = "ssh_password"
    }
}