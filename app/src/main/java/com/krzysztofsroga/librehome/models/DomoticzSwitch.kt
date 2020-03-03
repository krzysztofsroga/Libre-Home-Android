package com.krzysztofsroga.librehome.models

import android.util.Base64
import com.krzysztofsroga.librehome.connection.OnlineSwitches
import java.nio.charset.Charset

class DomoticzSwitch(
    val Name: String,
    val idx: Int,
    val SwitchType: String?,
    val Status: String,
    val Level: Int,
    val LevelNames: String
) {
    fun toLightSwitch(): LightSwitch {
        return when (SwitchType) {
            "Dimmer" -> LightSwitch.DimmableSwitch(Name, Status != "Off", Level, idx)
            "Selector" -> LightSwitch.SelectorSwitch(Name, Status != "Off", Level, Base64.decode(LevelNames, Base64.DEFAULT).toString(Charset.forName("UTF-8")).split("|"), idx)
            "On/Off" -> LightSwitch.SimpleSwitch(Name, Status != "Off", idx)
//            null -> LightSwitch.SimpleSwitch(Name, Status != "Off", idx)
            else -> LightSwitch.UnsupportedSwitch(Name, Status != "Off", idx, SwitchType)
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