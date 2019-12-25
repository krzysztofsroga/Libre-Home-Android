package com.krzysztofsroga.librehome.models

import com.google.gson.*
import java.lang.reflect.Type

sealed class LightSwitch(
    var name: String,
    var enabled: Boolean,
    var id: Int? = null
) {
    val type: String? = this::class.simpleName

    class SimpleSwitch(name: String, enabled: Boolean = false, id: Int? = null) : LightSwitch(name, enabled, id) {
        override fun toString(): String = "SimpleSwitch(name=$name, enabled=$enabled)"
    }

    class DimmableSwitch(name: String, enabled: Boolean = false, var dim: Int = 100, id: Int? = null) : LightSwitch(name, enabled, id) {
        override fun toString(): String = "DimmableSwitch(name=$name, enabled=$enabled, dim=$dim)"
    }

    companion object JsonSerialization : JsonDeserializer<LightSwitch>, JsonSerializer<LightSwitch> {
        override fun serialize(src: LightSwitch, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return context.serialize(src, src.javaClass)
        }

        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LightSwitch {
            val typeName = json.asJsonObject.get(LightSwitch::type.name).asString
            val type = LightSwitch::class.sealedSubclasses.find {
                it.simpleName == typeName
            }
            return context.deserialize(json, type!!.java)
        }
    }

}

