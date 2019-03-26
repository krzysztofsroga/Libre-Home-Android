package com.krzysztofsroga.librehome.ui.Switches

data class SwitchModel(
    var name: String,
    var state: Boolean = false
)

enum class SwitchType {
    Light,
    DimmableLight
}