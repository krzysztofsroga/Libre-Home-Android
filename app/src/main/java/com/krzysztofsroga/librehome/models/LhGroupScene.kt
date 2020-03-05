package com.krzysztofsroga.librehome.models

import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.connection.DomoticzService

sealed class LhGroupScene( //TODO create abstract class that connects LhGroupScene and LightSwitch
    var name: String,
    var id: Int? = null
) {
    val type: String? = this::class.simpleName
    abstract val icon: Int

    abstract suspend fun sendState(service: DomoticzService)

    class LhScene(name: String, id: Int) : LhGroupScene(name, id) {
        override val icon: Int
            get() = R.drawable.ic_landscape_black_24dp

        override suspend fun sendState(service: DomoticzService) {
            service.sendGroupState(id, "On")
        }
    }

    class LhGroup(name: String, id: Int, var enabled: Boolean) : LhGroupScene(name, id) {
        override val icon: Int
            get() =  R.drawable.ic_dashboard_black_24dp

        override suspend fun sendState(service: DomoticzService) {
            service.sendGroupState(id, if (enabled) "On" else "Off")
        }
    }

    class LhUnsupportedGroupScene(name: String?, id: Int?, val typeName: String?) : LhGroupScene(name ?: "Unnamed", id) {
        override val icon: Int
            get() =  R.drawable.ic_report_problem_black_24dp

        override suspend fun sendState(service: DomoticzService) {}
    }
}