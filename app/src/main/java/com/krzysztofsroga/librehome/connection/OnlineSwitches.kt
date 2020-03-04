package com.krzysztofsroga.librehome.connection

import android.util.Log
import com.krzysztofsroga.librehome.models.LhGroupScene
import com.krzysztofsroga.librehome.models.LightSwitch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OnlineSwitches(hostname: String) {

    private val service: DomoticzService = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://$hostname:${InternetConfiguration.defaultDomoticzPort}")
        .client(OkHttpClient().newBuilder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)).build())
        .build()
        .create(DomoticzService::class.java)

    suspend fun suspendGetAllSwitches(): List<LightSwitch> {
        return service.getSwitches().toSwitchStatesModel().items
    }

    suspend fun getAllGroupScenes(): List<LhGroupScene> {
        return service.getGroups().toGroupStatesModel().items
    }

    suspend fun suspendSendSwitchState(lightSwitch: LightSwitch) {
        lightSwitch.sendState(service)
//        val (cmd, dim) = when {
//            lightSwitch.enabled && lightSwitch is LightSwitch.DimmableSwitch -> "Set%20Level" to lightSwitch.dim
//            lightSwitch is LightSwitch.SelectorSwitch -> if (lightSwitch.enabled || lightSwitch.dim == 0) "Set%20Level" to lightSwitch.dim else "Off" to lightSwitch.dim
//            lightSwitch.enabled -> "On" to null
//            else -> "Off" to null
//        }
//        val response = service.sendSwitchState(lightSwitch.id, cmd, dim)
//        Log.d("Send", response.toString())
    }

    suspend fun sendGroupState(lhGroupScene: LhGroupScene) {
        lhGroupScene.sendState(service)
//        val cmd = if (lhGroupScene is LhGroupScene.LhGroup && !lhGroupScene.enabled) "Off" else "On"
//        val response = service.sendGroupState(lhGroupScene.id, cmd)
//        Log.d("Send", response.toString())
    }

    data class SwitchStatesModel(val name: String, val items: List<LightSwitch>)

    data class GroupStatesModel(val name: String, val items: List<LhGroupScene>)

    data class DomoticzResponse(val status: String, val title: String)
}

