package com.krzysztofsroga.librehome.models

import com.google.gson.*
import com.krzysztofsroga.librehome.connection.DomoticzService
import java.lang.reflect.Type

sealed class LightSwitch(
    var name: String,
    var enabled: Boolean,
    var id: Int? = null
) {
    val type: String? = this::class.simpleName

    abstract suspend fun sendState(service: DomoticzService)

    class SimpleSwitch(name: String, enabled: Boolean = false, id: Int? = null) : LightSwitch(name, enabled, id) {
        override suspend fun sendState(service: DomoticzService) {
            service.sendSwitchState(id, if(enabled) "On" else "Off")
        }

        override fun toString(): String = "SimpleSwitch(name=$name, enabled=$enabled)"
    }

    class DimmableSwitch(name: String, enabled: Boolean = false, var dim: Int = 100, id: Int? = null) : LightSwitch(name, enabled, id) {
        override suspend fun sendState(service: DomoticzService) {
            service.sendSwitchState(id, if (enabled) "Set%20Level" else "Off", dim)
        }

        override fun toString(): String = "DimmableSwitch(name=$name, enabled=$enabled, dim=$dim)"
    }

    class SelectorSwitch(name: String, enabled: Boolean = false, var dim: Int = 100, var levels: List<String>, id: Int? = null) : LightSwitch(name, enabled, id) {
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

//    companion object JsonSerialization : JsonDeserializer<LightSwitch>, JsonSerializer<LightSwitch> {
//        override fun serialize(src: LightSwitch, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
//            return context.serialize(src, src.javaClass)
//        }
//
//        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LightSwitch {
//            val typeName = json.asJsonObject.get(LightSwitch::type.name).asString
//            val type = LightSwitch::class.sealedSubclasses.find {
//                it.simpleName == typeName
//            }
//            return context.deserialize(json, type!!.java)
//        }
//    }

}

