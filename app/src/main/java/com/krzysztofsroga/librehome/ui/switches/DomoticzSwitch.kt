package com.krzysztofsroga.librehome.ui.switches

class DomoticzSwitch (
    val Name: String,
    val idx: Int,
    val IsDimmer: Boolean
) {
    fun toLightSwitch(): LightSwitch {
        return if (IsDimmer) {
            LightSwitch.DimmableSwitch(Name, true, 100)
        } else {
            LightSwitch.SimpleSwitch(Name, true)
        }
    }
}

data class DomoticzSwitches (
    val result: List<DomoticzSwitch>
) {
    fun toSwitchStatesModel(): OnlineSwitches.SwitchStatesModel {
        return OnlineSwitches.SwitchStatesModel("Domoticz Switches", result.map { it.toLightSwitch() })
    }
}