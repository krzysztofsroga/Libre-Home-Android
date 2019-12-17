package com.krzysztofsroga.librehome.models

import com.krzysztofsroga.librehome.connection.OnlineSwitches

class DomoticzSwitch(
    val Name: String,
    val idx: Int,
    val SwitchType: String,
    val Status: String,
    val Level: Int
) {
    fun toLightSwitch(): LightSwitch {
        return if (SwitchType == "Dimmer") {
            LightSwitch.DimmableSwitch(Name, Status != "Off", Level, idx)
        } else {
            LightSwitch.SimpleSwitch(Name, Status != "Off", idx)
        }
    }
}

data class DomoticzSwitches(
    val result: List<DomoticzSwitch>
) {
    fun toSwitchStatesModel(): OnlineSwitches.SwitchStatesModel {
        return OnlineSwitches.SwitchStatesModel("Domoticz Switches", result.map { it.toLightSwitch() })
    }
}