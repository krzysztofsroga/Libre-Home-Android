package com.krzysztofsroga.librehome.models

import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.connection.DomoticzService

sealed class LhGroupScene(id: Int, name: String) : LhComponent(id, name) {

    companion object {
        fun fromDomoticzComponent(dComp: DomoticzComponent): LhGroupScene {
            return dComp.run {
                when (Type) {
                    "Group" -> LhGroup(idx, Name, Status != "Off")
                    "Scene" -> LhScene(idx, Name)
                    else -> LhUnsupportedGroupScene(idx, Name, Type)
                }
            }
        }
    }

    class LhScene(id: Int, name: String) : LhGroupScene(id, name), HasButton {
        override val icon: Int = R.drawable.ic_landscape_black_24dp

        override suspend fun sendState(service: DomoticzService) {
            service.sendGroupState(id, "On")
        }
    }

    class LhGroup(id: Int, name: String, override var enabled: Boolean) : LhGroupScene(id, name), Switchable {
        override val icon: Int = R.drawable.ic_dashboard_black_24dp

        override suspend fun sendState(service: DomoticzService) {
            service.sendGroupState(id, if (enabled) "On" else "Off")
        }
    }

    class LhUnsupportedGroupScene(id: Int, name: String?, override val typeName: String?) : LhGroupScene(id, name ?: "Unnamed"), Unsupported {
        override val icon: Int = R.drawable.ic_report_problem_black_24dp

        override suspend fun sendState(service: DomoticzService) {}
    }
}