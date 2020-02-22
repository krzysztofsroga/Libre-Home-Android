package com.krzysztofsroga.librehome.connection

import android.util.Log
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

    suspend fun suspendSendSwitchState(lightSwitch: LightSwitch) {
        val (cmd, dim) = when {
            lightSwitch.enabled && lightSwitch is LightSwitch.DimmableSwitch -> "Set%20Level" to lightSwitch.dim
            lightSwitch is LightSwitch.SelectorSwitch -> if (lightSwitch.enabled) "Set%20Level" to lightSwitch.dim else "Off" to lightSwitch.dim
            lightSwitch.enabled -> "On" to null
            else -> "Off" to null
        }
        val response = service.sendSwitchState(lightSwitch.id, cmd, dim)
        Log.d("Send", response.toString())
    }

    data class SwitchStatesModel(val name: String, val items: List<LightSwitch>)

    data class DomoticzResponse(val status: String, val title: String)
}

