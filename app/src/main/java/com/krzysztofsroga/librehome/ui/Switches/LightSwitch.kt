package com.krzysztofsroga.librehome.ui.Switches

data class LightSwitch(
    var name: String,
    var state: Boolean = false
)

enum class SwitchType {
    Light,
    DimmableLight
}