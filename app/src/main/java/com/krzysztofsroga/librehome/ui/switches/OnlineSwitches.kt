package com.krzysztofsroga.librehome.ui.switches

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.krzysztofsroga.librehome.InternetConfiguration


class OnlineSwitches {
    fun initialize() {
        configureFuel()
    }

    fun sendSwitchState(lightSwitch: LightSwitch) {
        val logTag = "switches-post"
        val cmd = if (lightSwitch.enabled) if(lightSwitch is LightSwitch.DimmableSwitch) "Set%20Level&level=${lightSwitch.dim}" else "On" else "Off"

//        val cmd = if (lightSwitch.enabled) "On" else "Off"
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
//
//        if (lightSwitch is LightSwitch.DimmableSwitch && lightSwitch.enabled) {
//            Fuel.get( "json.htm?type=command&param=switchlight&idx=${lightSwitch.id}&switchcmd=Set%20Level&Level=${lightSwitch.dim}").responseString { _, _, result ->
//                when (result) {
//                    is Result.Failure -> {
//                        Log.e(logTag, "failed: ${result.error}")
//                    }
//                    is Result.Success -> {
//                        Log.d(logTag, "success: ${result.value}")
//                    }
//                }
//            }
//        }
    }

    fun sendSwitchStateOld(lightSwitch: LightSwitch) {
        val logTag = "switches-post"
        val json = Gson().toJson(lightSwitch)

        Fuel.post("/postSwitchChange").timeout(1000).body(json).responseString { request, response, result ->
            Log.v(logTag, "request: $request")
            Log.v(logTag, "response: $response")
            when (result) {
                is Result.Failure -> {
                    Log.d(logTag, "failed: ${result.error}")
                    //TODO do more than logging
                }
                is Result.Success -> {
                    Log.d(logTag, "success: ${result.value}")
                    //TODO something
                }
            }
        }
    }

    fun getAllSwitches(callback: (List<LightSwitch>) -> Unit) {
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
                    callback(obj.items)
                }
            }
        }
    }

    fun getAllSwitchesOld(callback: (List<LightSwitch>) -> Unit) {
        val logTag = "switches-get-all"
        Fuel.get("/switches").responseString { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    Log.e(logTag, "failed: ${result.error}")
                    //TODO do more than logging
                }
                is Result.Success -> {
                    Log.d(logTag, "success: ${result.value}")
                    val gson = GsonBuilder().registerTypeAdapter(LightSwitch::class.java, LightSwitch.JsonSerialization)
                        .create()
                    val obj = gson.fromJson<SwitchStatesModel>(result.value, SwitchStatesModel::class.java)
                    Log.d(logTag, "object: ${obj.items.joinToString { "(${it.type}: ${it.name})" }}")
                    callback(obj.items) //TODO remove this crappy callback, use postValue of LiveData instead
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