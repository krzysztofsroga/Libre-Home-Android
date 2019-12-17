package com.krzysztofsroga.librehome.connection

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.google.gson.GsonBuilder
import com.krzysztofsroga.librehome.models.DomoticzSwitches
import com.krzysztofsroga.librehome.models.LightSwitch


class OnlineSwitches {
    fun initialize() {
        configureFuel()
    }

    fun sendSwitchState(lightSwitch: LightSwitch) {
        val logTag = "switches-post"
        val cmd = if (lightSwitch.enabled) if (lightSwitch is LightSwitch.DimmableSwitch) "Set%20Level&level=${lightSwitch.dim}" else "On" else "Off"

//        val cmd = if (lightSwitch.enabled) "On" else "Off"
        val path = "json.htm?type=command&param=switchlight&idx=${lightSwitch.id}&switchcmd=$cmd" //TODO pass parameters
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

    fun getAllSwitches(callback: (List<LightSwitch>) -> Unit) {
        val logTag = "switches-get-domoticz"
        val path = "json.htm?type=devices&filter=lights&used=true&order=Name"
        Fuel.get(path).responseString { _, _, result ->
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
                    callback(obj.items)
                }
            }
        }
    }

    companion object {
        internal fun configureFuel() { //TODO move configuration in different place
            FuelManager.instance.baseHeaders = mapOf("Content-Type" to "application/json")
            FuelManager.instance.basePath = InternetConfiguration.fullPath
            Log.d("Fuel initialization", InternetConfiguration.fullPath)
        }
    }


    data class SwitchStatesModel(val name: String, val items: List<LightSwitch>)
}