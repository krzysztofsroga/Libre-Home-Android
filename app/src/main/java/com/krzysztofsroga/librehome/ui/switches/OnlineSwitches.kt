package com.krzysztofsroga.librehome.ui.switches

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.google.gson.*
import com.krzysztofsroga.librehome.InternetConfiguration
import java.lang.reflect.Type


class OnlineSwitches {
    fun initialize() {
        configureFuel()
    }

    object Deserializer : JsonDeserializer<LightSwitch> {
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): LightSwitch {
            val typeName = json.asJsonObject.get(LightSwitch::type.name).asString
            val type = LightSwitch::class.sealedSubclasses.find {
                it.simpleName == typeName
            }
            val lightSwitch = context.deserialize<LightSwitch>(json, type!!.java)
            Log.d("dupa", lightSwitch.type)
            return lightSwitch
        }

    }

    fun sendSwitchState(lightSwitch: LightSwitch) {
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
        val logTag = "switches-get-all"

        Fuel.get("/switches").responseString { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    Log.e(logTag, "failed: ${result.error}")
                    //TODO do more than logging
                }
                is Result.Success -> {
                    Log.d(logTag, "success: ${result.value}")
                    val gson = GsonBuilder().registerTypeAdapter(LightSwitch::class.java, Deserializer).create()
                    val obj = gson.fromJson<SwitchStatesModel>(result.value, SwitchStatesModel::class.java)
                    Log.d(logTag, "object: ${obj.items.joinToString { "(${it.type}: ${it.name})" }}")
                    callback(obj.items) //TODO remove this crappy callback, use postValue of LiveData instead
                }
            }
        }

    }


    private fun configureFuel() { //TODO move configuration in different place
        FuelManager.instance.baseHeaders = mapOf("Content-Type" to "application/json")
        FuelManager.instance.basePath = InternetConfiguration.fullPath
        Log.d("Fuel initialization", InternetConfiguration.fullPath)
    }

    data class SwitchStatesModel(val name: String, val items: List<LightSwitch>)
}