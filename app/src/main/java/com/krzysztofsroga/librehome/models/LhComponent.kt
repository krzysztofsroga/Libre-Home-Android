package com.krzysztofsroga.librehome.models

import com.krzysztofsroga.librehome.connection.DomoticzService

abstract class LhComponent(val id: Int, val name: String) {
    abstract val icon: Int
    abstract suspend fun sendState(service: DomoticzService)

    interface Switchable {
        var enabled: Boolean
    }

    interface Dimmable {
        var dim: Int
    }

    interface SensorData {
        var state: String
    }

    interface SensorDataUpdate {
        var state: String
        var update: String
    }

    interface HasButton

    interface SimpleName

    interface Unsupported {
        val typeName: String?
    }
}