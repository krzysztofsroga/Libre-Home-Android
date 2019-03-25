package com.krzysztofsroga.librehome

data class Switch(
    var name: String,
    var state: Boolean = false
)

enum class SwitchType {
    Light,
    DimmableLight
}