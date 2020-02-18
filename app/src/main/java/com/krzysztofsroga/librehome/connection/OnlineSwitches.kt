package com.krzysztofsroga.librehome.connection

import com.krzysztofsroga.librehome.models.LightSwitch

class OnlineSwitches(hostname: String) {

    private val retrofitConnection: RetrofitConnection = RetrofitConnection(hostname)

    suspend fun suspendSendSwitchState(lightSwitch: LightSwitch) {
        retrofitConnection.suspendSendSwitchState(lightSwitch)
    }

    suspend fun suspendGetAllSwitches(): List<LightSwitch> {
        return retrofitConnection.suspendGetAllSwitches()
    }


    data class SwitchStatesModel(val name: String, val items: List<LightSwitch>)
}