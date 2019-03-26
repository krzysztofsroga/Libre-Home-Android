package com.krzysztofsroga.librehome.ui.Switches

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result
import com.google.gson.*
import com.krzysztofsroga.librehome.InternetConfiguration
import java.lang.reflect.Type


class OnlineSwitches {
    fun initialize() {
        configureFuel()
    }

    fun sendSwitchState(lightSwitch: LightSwitch) {
        val logTag = "switches-post"
//        val json = Gson().toJson(contact)

//        Fuel.post("/contacts").timeout(timeout).body(json).responseString { request: Request, response: Response, result: Result<String, FuelError> ->
//            Log.v(logTag, "request: $request")
//            Log.v(logTag, "response: $response")
//            when (result) {
//                is Result.Failure -> {
//                    Log.d(logTag, "failed: ${result.error}")
//                    errorMessage.postValue("failed: ${result.error}")
//                }
//                is Result.Success -> {
//                    Log.d(logTag, "success: ${result.value}")
//                    getAll() //TODO remove this line, post shouldn't call get
//                }
//            }
//        }
    }

    fun getAllSwitches(callback: (List<LightSwitch>) -> Unit) {
        val logTag = "switches-get-all"
        // TODO LightSwitch::class.sealedSubclasses
//        Fuel.get("/switches")
//            .responseObject<SwitchStatesModel> { _, _, result ->
//                when (result) {
//                    is Result.Failure -> {
//                        Log.e(logTag, "failed: ${result.error}")
//                        //TODO do more than logging
//                    }
//                    is Result.Success -> {
//                        Log.d(logTag, "success: ${result.value}")
//                        callback(result.value.items) //TODO instead of creepy callback use postValue of LiveData
//                    }
//                }
//            }

//        Fuel.get("/switches").responseString { _, _, result ->
//            when (result) {
//                is Result.Failure -> {
//                    Log.e(logTag, "failed: ${result.error}")
//                    //TODO do more than logging
//                }
//                is Result.Success -> {
//                    Log.d(logTag, "success: ${result.value}")
//                }
//            }
//        }


        Fuel.get("/switches").responseString  { _, _, result ->
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
                    callback(obj.items)
                }
            }
        }

    }

    object Deserializer: JsonDeserializer<LightSwitch> {
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


    private fun configureFuel() { //TODO move configuration in different place
        FuelManager.instance.baseHeaders = mapOf("Content-Type" to "application/json")
        FuelManager.instance.basePath = InternetConfiguration.fullPath
        Log.d("Fuel initialization", InternetConfiguration.fullPath)
    }

    data class SwitchStatesModel(val name: String, val items: List<LightSwitch>)
}