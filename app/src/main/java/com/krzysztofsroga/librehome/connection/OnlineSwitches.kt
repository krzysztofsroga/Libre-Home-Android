package com.krzysztofsroga.librehome.connection

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.google.gson.GsonBuilder
import com.krzysztofsroga.librehome.AppConfig
import com.krzysztofsroga.librehome.models.DomoticzSwitches
import com.krzysztofsroga.librehome.models.LightSwitch
import java.net.MalformedURLException


class OnlineSwitches(private val hostname: String) {

    init {
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
        try {
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
        } catch (e: MalformedURLException) {
            Log.d(logTag, "Malformed url exception")
            Log.d(logTag, e.message)
        } //TODO ASAP pass error further and show as toast
    }

    private fun configureFuel() {
        val fullPath = "http://$hostname:${InternetConfiguration.defaultDomoticzPort}" //TODO allow port config?
        FuelManager.instance.baseHeaders = mapOf("Content-Type" to "application/json")
        FuelManager.instance.basePath = fullPath
        Log.d("Fuel initialization", fullPath)
    }

    data class SwitchStatesModel(val name: String, val items: List<LightSwitch>)
}