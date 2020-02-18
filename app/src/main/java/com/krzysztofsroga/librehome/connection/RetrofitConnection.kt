package com.krzysztofsroga.librehome.connection

import com.krzysztofsroga.librehome.models.DomoticzSwitches
import com.krzysztofsroga.librehome.models.LightSwitch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class RetrofitConnection(hostname: String) {
    private val service: DomoticzService

    init {
        val fullPath = "http://$hostname:${InternetConfiguration.defaultDomoticzPort}" //TODO allow port config?
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(fullPath)
            .build()

        service = retrofit.create(DomoticzService::class.java)
//        FuelManager.instance.baseHeaders = mapOf("Content-Type" to "application/json")

    }

    suspend fun suspendGetAllSwitches(): List<LightSwitch> {
        return service.getSwitches().toSwitchStatesModel().items
    }

    suspend fun suspendSendSwitchState(lightSwitch: LightSwitch) {
        val cmd = when {
            lightSwitch.enabled && lightSwitch is LightSwitch.DimmableSwitch -> "Set%20Level&level=${lightSwitch.dim}"
            lightSwitch.enabled -> "On"
            else -> "Off"
        }
//        val cmd = if (lightSwitch.enabled) if (lightSwitch is LightSwitch.DimmableSwitch) "Set%20Level&level=${lightSwitch.dim}" else "On" else "Off"
        service.sendSwitchState(lightSwitch.id, cmd)
    }
}

interface DomoticzService {
    @GET("json.htm?type=devices&filter=lights&used=true&order=Name")
    suspend fun getSwitches(): DomoticzSwitches

    @GET("json.htm?type=command&param=switchlight")
    suspend fun sendSwitchState(@Query("idx") lightSwitchId: Int?, @Query("switchcmd") switchCmd: String) //TODO ID should not be null
}