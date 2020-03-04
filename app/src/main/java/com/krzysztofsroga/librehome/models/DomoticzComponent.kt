package com.krzysztofsroga.librehome.models

import android.util.Base64
import com.krzysztofsroga.librehome.connection.OnlineSwitches
import java.nio.charset.Charset

class DomoticzComponent(
    val Name: String,
    val idx: Int,
    val SwitchType: String?,
    val Type: String?,
    val SubType: String?,
    val Status: String,
    val Level: Int,
    val LevelNames: String
) {
//    fun toLightSwitch(): LightSwitch {
//        return when (SwitchType) {
//            "Dimmer" -> LightSwitch.DimmableSwitch(Name, Status != "Off", Level, idx)
//            "Selector" -> LightSwitch.SelectorSwitch(Name, Status != "Off", Level, Base64.decode(LevelNames, Base64.DEFAULT).toString(Charset.forName("UTF-8")).split("|"), idx)
//            "On/Off" -> LightSwitch.SimpleSwitch(Name, Status != "Off", idx)
//            "Push Off Button", "Push On Button" -> LightSwitch.PushButtonSwitch(Name, idx)
//            "Blinds Percentage", "Percentage" -> LightSwitch.PercentageSwitch(Name, Level, idx)
////            null -> LightSwitch.SimpleSwitch(Name, Status != "Off", idx)
//            else -> LightSwitch.UnsupportedSwitch(Name, Status != "Off", idx, SwitchType)
//        }
//    }
}

//data class DomoticzSwitches(val result: List<DomoticzComponent>)