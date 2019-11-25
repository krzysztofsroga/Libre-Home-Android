package com.krzysztofsroga.librehome.ui.music

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.GsonBuilder
import com.krzysztofsroga.librehome.ui.switches.DomoticzSwitches
import com.krzysztofsroga.librehome.ui.switches.LightSwitch
import com.krzysztofsroga.librehome.ui.switches.OnlineSwitches

class MusicSwitches {
    fun initialize() {
        OnlineSwitches.configureFuel()
    }


    fun getLedSwitches(callback: (List<LightSwitch.DimmableSwitch>) -> Unit) {
        val logTag = "switches-get-domoticz"
        Fuel.get("json.htm?type=devices&filter=lights&used=true&order=Name").responseString { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    Log.e(logTag, "failed: ${result.error}")
                }
                is Result.Success -> {
                    Log.d(logTag, "success: ${result.value}")
                    val gson = GsonBuilder().create()
                    val dObj = gson.fromJson<DomoticzSwitches>(result.value, DomoticzSwitches::class.java)
                    val obj = dObj.toSwitchStatesModel()
                    Log.d(logTag, "object: ${obj.items.joinToString { "(${it.type}: ${it.name}), id: ${it.id}\n" }}")
                    callback(obj.items.filterIsInstance<LightSwitch.DimmableSwitch>() )
                }
            }
        }
    }


    fun sendSwitchState(lightSwitch: LightSwitch) {
        val logTag = "switches-post"
        val cmd =
            if (lightSwitch.enabled) if (lightSwitch is LightSwitch.DimmableSwitch) "Set%20Level&level=${lightSwitch.dim}" else "On" else "Off"

        val path = "json.htm?type=command&param=switchlight&idx=${lightSwitch.id}&switchcmd=$cmd"
        Log.d(logTag, path)
        Fuel.get(path).responseString { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    Log.e(logTag, "failed: ${result.error}")
                }
                is Result.Success -> {
                    Log.d(logTag, "success: ${result.value}")
                }
            }
        }
    }

}