package com.krzysztofsroga.librehome.models

import com.google.gson.*
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.connection.DomoticzService
import java.lang.reflect.Type

sealed class LightSwitch(
    var name: String,
    var enabled: Boolean,
    var id: Int? = null
) {
    val type: String? = this::class.simpleName
    abstract val icon: Int

    abstract suspend fun sendState(service: DomoticzService)

    class UnsupportedSwitch(name: String?, enabled: Boolean?, id: Int?, val typeName: String?) : LightSwitch(name?: "Unnamed", enabled ?: false, id){
        override val icon: Int
            get() =  R.drawable.ic_report_problem_black_24dp

        override suspend fun sendState(service: DomoticzService) {
            service.sendSwitchState(id, if(enabled) "On" else "Off")
        }

        override fun toString(): String = "UnsupportedSwitch(name=$name, enabled=$enabled)"
    }

    class SimpleSwitch(name: String, enabled: Boolean = false, id: Int? = null) : LightSwitch(name, enabled, id) {
        override val icon: Int
            get() =  R.drawable.light

        override suspend fun sendState(service: DomoticzService) {
            service.sendSwitchState(id, if(enabled) "On" else "Off")
        }

        override fun toString(): String = "SimpleSwitch(name=$name, enabled=$enabled)"
    }

    class PushButtonSwitch(name: String, id: Int? = null) : LightSwitch(name, false, id) {
        override val icon: Int
            get() = R.drawable.icons8_push_button

        override suspend fun sendState(service: DomoticzService) {
            service.sendSwitchState(id, "On")
        }

        override fun toString(): String = "PushButton(name=$name)"
    }

    class DimmableSwitch(name: String, enabled: Boolean = false, var dim: Int = 100, id: Int? = null) : LightSwitch(name, enabled, id) {
        override val icon: Int
            get() =  R.drawable.light_dim

        override suspend fun sendState(service: DomoticzService) {
            service.sendSwitchState(id, if (enabled) "Set%20Level" else "Off", dim)
        }

        override fun toString(): String = "DimmableSwitch(name=$name, enabled=$enabled, dim=$dim)"
    }

    class SelectorSwitch(name: String, enabled: Boolean = false, var dim: Int = 100, var levels: List<String>, id: Int? = null) : LightSwitch(name, enabled, id) {
        override val icon: Int
            get() =  R.drawable.ic_format_list_bulleted_black_24dp

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

    class PercentageSwitch(name: String, var dim: Int = 0, id: Int? = null) : LightSwitch(name, false, id) {
        override val icon: Int
            get() = R.drawable.icons8_jalousie

        override suspend fun sendState(service: DomoticzService) {}
    }
}

