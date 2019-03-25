package com.krzysztofsroga.librehome

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result

data class SwitchStatesModel(val name: String, val items: List<Switch>)

class OnlineSwitches {
    fun initialize() {
        configureFuel()
    }

    fun sendSwitchState(switch: Switch) {
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

    fun getAllSwitches(callback: (List<Switch>) -> Unit) {
        val logTag = "switches-get-all"

        Fuel.get("/switches")
            .responseObject<SwitchStatesModel> { _, _, result: Result<SwitchStatesModel, FuelError> ->
                when (result) {
                    is Result.Failure -> {
                        Log.e(logTag, "failed: ${result.error}")
                        //TODO do more than logging
                    }
                    is Result.Success -> {
                        Log.d(logTag, "success: ${result.value}")
                        callback(result.value.items) //TODO instead of creepy callback use postValue of LiveData
                    }
                }
            }
    }


    private fun configureFuel() { //TODO move configuration in different place
        FuelManager.instance.baseHeaders = mapOf("Content-Type" to "application/json")
        FuelManager.instance.basePath = InternetConfiguration.fullPath
        Log.d("Fuel initialization", InternetConfiguration.fullPath)
    }
}