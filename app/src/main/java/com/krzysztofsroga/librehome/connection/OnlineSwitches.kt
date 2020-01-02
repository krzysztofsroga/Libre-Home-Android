package com.krzysztofsroga.librehome.connection

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.coroutines.awaitStringResult
import com.github.kittinunf.result.failure
import com.google.gson.GsonBuilder
import com.krzysztofsroga.librehome.models.DomoticzSwitches
import com.krzysztofsroga.librehome.models.LightSwitch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class OnlineSwitches(private val hostname: String) {

    init {
        configureFuel()
    }

    suspend fun suspendSendSwitchState(lightSwitch: LightSwitch) {
        val cmd = if (lightSwitch.enabled) if (lightSwitch is LightSwitch.DimmableSwitch) "Set%20Level&level=${lightSwitch.dim}" else "On" else "Off"
        val path = "json.htm?type=command&param=switchlight&idx=${lightSwitch.id}&switchcmd=$cmd" //TODO pass parameters
        withContext(Dispatchers.IO) {
            Fuel.get(path).awaitStringResult().failure { throw it }
        }
    }

    suspend fun suspendGetAllSwitches(): List<LightSwitch> {
        val path = "json.htm?type=devices&filter=lights&used=true&order=Name"
        return withContext(Dispatchers.IO) {
            Fuel.get(path).awaitStringResult().fold(
                { resultString ->
                    val gson = GsonBuilder().create()
                    val dObj = gson.fromJson<DomoticzSwitches>(resultString, DomoticzSwitches::class.java)
                    val obj = dObj.toSwitchStatesModel()
                    obj.items
                },
                { throw it })
        }
    }

    private fun configureFuel() {
        val fullPath = "http://$hostname:${InternetConfiguration.defaultDomoticzPort}" //TODO allow port config?
        FuelManager.instance.baseHeaders = mapOf("Content-Type" to "application/json")
        FuelManager.instance.basePath = fullPath
        Log.d("Fuel initialization", fullPath)
    }

    data class SwitchStatesModel(val name: String, val items: List<LightSwitch>)
}