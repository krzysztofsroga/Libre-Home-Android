package com.krzysztofsroga.librehome.models

import android.util.Base64
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.connection.DomoticzService
import java.nio.charset.Charset

sealed class LhDevice(id: Int, name: String) : LhComponent(id, name) {

    companion object {
        fun fromDomoticzComponent(dComp: DomoticzComponent): LhDevice {
            return dComp.run {
                if (SwitchType != null) {
                    when (SwitchType) {
                        "On/Off" -> LhSimpleSwitch(idx, Name, Status != "Off")
                        "Push Off Button", "Push On Button" -> LhPushButton(idx, Name)
                        "Dimmer" -> LhDimmableSwitch(idx, Name, Status != "Off", Level)
                        "Selector" -> LhSelectorSwitch(idx, Name, Status != "Off", Level, Base64.decode(LevelNames, Base64.DEFAULT).toString(Charset.forName("UTF-8")).split("|"))
                        "Blinds Percentage" -> LhBlindsPercentage(idx, Name, Level)
                        "Motion Sensor" -> LhMotionSensor(idx, Name, Data)
                        "Door Lock Inverted", "Door Lock" -> LhDoorLock(idx, Name, Data)
                        "Smoke Detector" -> LhSmokeDetector(idx, Name, Data)
                        "Media Player" -> LhMediaPlayer(idx, Name, Status)
                        else -> LhUnsupported(idx, Name, SwitchType)
                    }
                }
                else {
                    when (Type) {
                        "Lux" -> LhLuxSensor(idx, Name, Data)
                        "Temp + Humidity", "Temp" -> LhTempSensor(idx, Name, Data)
                        "General" -> LhGeneralSensor(idx, Name, Data)
                        else -> LhUnsupported(idx, Name, Type)
                    }
                }
            }
        }
    }

    class LhSimpleSwitch(id: Int, name: String, override var enabled: Boolean) : LhDevice(id, name), Switchable {
        override val icon: Int = R.drawable.light

        override suspend fun sendState(service: DomoticzService) {
            service.sendDeviceState(id, if (enabled) "On" else "Off")
        }
    }

    class LhPushButton(id: Int, name: String) : LhDevice(id, name), HasButton {
        override val icon: Int = R.drawable.icons8_push_button

        override suspend fun sendState(service: DomoticzService) {
            service.sendDeviceState(id, "On")
        }
    }

    class LhDimmableSwitch(id: Int, name: String, override var enabled: Boolean, override var dim: Int) : LhDevice(id, name), Switchable, Dimmable {
        override val icon: Int = R.drawable.light_dim

        override suspend fun sendState(service: DomoticzService) {
            service.sendDeviceState(id, if (enabled) "Set%20Level" else "Off", dim)
        }
    }

    class LhSelectorSwitch(id: Int, name: String, override var enabled: Boolean, var dim: Int, var levels: List<String>) : LhDevice(id, name), Switchable {
        override val icon: Int = R.drawable.ic_format_list_bulleted_black_24dp

        var selectedId
            get() = dim / 10
            set(value) {
                dim = value * 10
            }

        override suspend fun sendState(service: DomoticzService) {
            service.sendDeviceState(id, if (enabled) "Set%20Level" else "Off", dim)
        }
    }

    class LhBlindsPercentage(id: Int, name: String, override var dim: Int) : LhDevice(id, name), Dimmable, SimpleName {

        override val icon: Int = R.drawable.icons8_jalousie

        override suspend fun sendState(service: DomoticzService) {
            service.sendDeviceState(id, "Set%20Level", dim)
        }
    }

    class LhMotionSensor(id: Int, name: String,  override var state: String) : LhDevice(id, name), SimpleSensor {
        override val icon: Int = R.drawable.ic_directions_run_black_24dp

        override suspend fun sendState(service: DomoticzService) {}
    }

    class LhDoorLock(id: Int, name: String,  override var state: String) : LhDevice(id, name), SimpleSensor {
        override val icon: Int = R.drawable.ic_lock_black_24dp

        override suspend fun sendState(service: DomoticzService) {} //TODO We do not need method with sensors?
    }

    class LhSmokeDetector(id: Int, name: String, override var state: String) : LhDevice(id, name), SimpleSensor {
        override val icon: Int = R.drawable.ic_whatshot_black_24dp

        override suspend fun sendState(service: DomoticzService) {} //TODO We do not need method with sensors?
    }

    class LhMediaPlayer(id: Int, name: String, override var state: String) : LhDevice(id, name), SimpleSensor {
        override val icon: Int = R.drawable.ic_movie_black_24dp

        override suspend fun sendState(service: DomoticzService) {} //TODO We do not need method with sensors?
    }

    class LhLuxSensor(id: Int, name: String, override var state: String) : LhDevice(id, name), SimpleSensor {
        override val icon: Int = R.drawable.ic_wb_sunny_black_24dp

        override suspend fun sendState(service: DomoticzService) {} //TODO We do not need method with sensors?
    }

    class LhTempSensor(id: Int, name: String, override var state: String) : LhDevice(id, name), SimpleSensor {
        override val icon: Int = R.drawable.ic_beach_access_black_24dp

        override suspend fun sendState(service: DomoticzService) {} //TODO We do not need method with sensors?
    }

    class LhGeneralSensor(id: Int, name: String, override var state: String) : LhDevice(id, name), SimpleSensor {
        override val icon: Int = R.drawable.ic_developer_board_black_24dp

        override suspend fun sendState(service: DomoticzService) {} //TODO We do not need method with sensors?
    }

    class LhUnsupported(id: Int, name: String?, override val typeName: String?) : LhDevice(id, name ?: "Unnamed"), Unsupported {
        override val icon: Int = R.drawable.ic_report_problem_black_24dp

        override suspend fun sendState(service: DomoticzService) {}
    }
}

