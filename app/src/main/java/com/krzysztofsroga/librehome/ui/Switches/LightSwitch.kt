package com.krzysztofsroga.librehome.ui.Switches

sealed class LightSwitch(
    var name: String,
    var enabled: Boolean
) {
    val type: String? = this::class.simpleName
    class SimpleSwitch(name: String, enabled: Boolean = false): LightSwitch(name, enabled)
    class DimmableSwitch(name: String, enabled: Boolean = false, val dim: Int = 100): LightSwitch(name, enabled)
    //TODO think if dimmable and other features shouldn't be interfaces. But then it'll be a lot harder to deserialize them
}

