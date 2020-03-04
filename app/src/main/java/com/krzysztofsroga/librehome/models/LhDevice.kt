package com.krzysztofsroga.librehome.models

import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.connection.DomoticzService

sealed class LhDevice(id: Int, name: String) : LhComponent(id, name) {
    abstract fun isIdentified(identifier: ComponentIdentifier): Boolean
    abstract suspend fun sendState(service: DomoticzService)

    class LhSimpleSwitch(id: Int, name: String, override var enabled: Boolean) : LhDevice(id, name), Switchable {
        override val icon: Int = R.drawable.light

        override fun isIdentified(identifier: ComponentIdentifier): Boolean = identifier.SwitchType == "On/Off"

        override suspend fun sendState(service: DomoticzService) {
            service.sendSwitchState(id, if (enabled) "On" else "Off")
        }

        override fun toString(): String = "LhSimpleSwitch(name=$name, enabled=$enabled)"
    }

    class LhPushButton(id: Int, name: String) : LhDevice(id, name) {
        override val icon: Int = R.drawable.icons8_push_button

        override fun isIdentified(identifier: ComponentIdentifier): Boolean = identifier.SwitchType in listOf("Push Off Button", "Push On Button")

        override suspend fun sendState(service: DomoticzService) {
            service.sendSwitchState(id, "On")
        }

        override fun toString(): String = "PushButton(name=$name)"
    }

    class LhDimmableSwitch(id: Int, name: String, override var enabled: Boolean, override var dim: Int) : LhDevice(id, name), Switchable, Dimmable {
        override val icon: Int = R.drawable.light_dim

        override fun isIdentified(identifier: ComponentIdentifier): Boolean = identifier.SwitchType == "Dimmer"

        override suspend fun sendState(service: DomoticzService) {
            service.sendSwitchState(id, if (enabled) "Set%20Level" else "Off", dim)
        }

        override fun toString(): String = "DimmableSwitch(name=$name, enabled=$enabled, dim=$dim)"
    }

    class LhSelectorSwitch(id: Int, name: String, override var enabled: Boolean, override var dim: Int, var levels: List<String>) : LhDevice(id, name), Switchable, Dimmable {
        override val icon: Int = R.drawable.ic_format_list_bulleted_black_24dp

        override fun isIdentified(identifier: ComponentIdentifier): Boolean = identifier.SwitchType == "Selector"

        var selectedId
            get() = dim/10
            set(value) {
                dim = value * 10
            }

        override suspend fun sendState(service: DomoticzService) {
            service.sendSwitchState(id, if (enabled) "Set%20Level" else "Off", dim)
        }

        override fun toString(): String = "SelectorSwitch(name=$name, enabled=$enabled, dim=$dim, level=${levels[selectedId]})"
    }

    class LhBlindsPercentage(id: Int, name: String, override var dim: Int) : LhDevice(id, name), Dimmable {

        override val icon: Int = R.drawable.icons8_jalousie

        override fun isIdentified(identifier: ComponentIdentifier): Boolean = identifier.SwitchType == "Blinds Percentage"

        override suspend fun sendState(service: DomoticzService) {}
    }


    class LhUnsupported(id: Int, name: String?, val typeName: String?) : LhDevice(id, name ?: "Unnamed") {
        override val icon: Int = R.drawable.ic_report_problem_black_24dp

        override fun isIdentified(identifier: ComponentIdentifier): Boolean = true

        override suspend fun sendState(service: DomoticzService) {}

        override fun toString(): String = "LhUnsupported(name=$name, type=$typeName)"
    }
}

