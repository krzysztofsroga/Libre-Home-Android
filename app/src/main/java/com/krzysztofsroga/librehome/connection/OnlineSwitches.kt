package com.krzysztofsroga.librehome.connection

import com.krzysztofsroga.librehome.models.LightSwitch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OnlineSwitches(hostname: String) {

    private val service: DomoticzService = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://$hostname:${InternetConfiguration.defaultDomoticzPort}")
        .build()
        .create(DomoticzService::class.java)

    suspend fun suspendGetAllSwitches(): List<LightSwitch> {
        return service.getSwitches().toSwitchStatesModel().items
    }

    suspend fun suspendSendSwitchState(lightSwitch: LightSwitch) {
        val cmd = when {
            lightSwitch.enabled && lightSwitch is LightSwitch.DimmableSwitch -> "Set%20Level&level=${lightSwitch.dim}"
            lightSwitch.enabled -> "On"
            else -> "Off"
        }
        service.sendSwitchState(lightSwitch.id, cmd)
    }

    data class SwitchStatesModel(val name: String, val items: List<LightSwitch>)
}