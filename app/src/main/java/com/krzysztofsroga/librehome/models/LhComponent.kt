package com.krzysztofsroga.librehome.models

abstract class LhComponent(val id: Int, val name: String) {
    abstract val icon: Int

    interface Switchable {
        var enabled: Boolean
    }

    interface Dimmable {
        var dim: Int
    }
}