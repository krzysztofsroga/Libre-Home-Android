package com.krzysztofsroga.librehome.models

import android.util.Base64
import com.krzysztofsroga.librehome.models.LhDevice.*
import com.krzysztofsroga.librehome.models.LhSensor.*
import java.nio.charset.Charset

abstract class LhAbstractDevice(id: Int, name: String) : LhComponent(id, name){

    companion object {
        fun fromDomoticzComponent(dComp: DomoticzComponent): LhAbstractDevice {
            return dComp.run {
                when (SwitchType) {
                    "On/Off" -> LhSimpleSwitch(idx, Name, Status != "Off")
                    "Push Off Button", "Push On Button" -> LhPushButton(idx, Name)
                    "Dimmer" -> LhDimmableSwitch(idx, Name, Status != "Off", Level)
                    "Selector" -> LhSelectorSwitch(idx, Name, Status != "Off", Level, Base64.decode(LevelNames, Base64.DEFAULT).toString(Charset.forName("UTF-8")).split("|"))
                    "Blinds Percentage" -> LhBlindsPercentage(idx, Name, Level)
                    "Motion Sensor" -> LhMotionSensor(idx, Name, Data)
                    "Door Lock Inverted", "Door Lock" -> LhDoorLockSensor(idx, Name, Data)
                    "Smoke Detector" -> LhSmokeDetector(idx, Name, Data)
                    "Media Player" -> LhMediaPlayerSensor(idx, Name, Status)
                    null -> when (Type) {
                        "Lux" -> LhLuxSensor(idx, Name, Data)
                        "Temp + Humidity", "Temp" -> LhTempSensor(idx, Name, Data)
                        "General" -> LhGeneralSensor(idx, Name, Data)
                        else -> LhUnsupported(idx, Name, Type)
                    }
                    else -> LhUnsupported(idx, Name, SwitchType)
                }
            }
        }
    }
}