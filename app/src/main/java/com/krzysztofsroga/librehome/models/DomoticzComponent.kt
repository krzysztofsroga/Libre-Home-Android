package com.krzysztofsroga.librehome.models

class DomoticzComponent(
    val Name: String,
    val idx: Int,
    val SwitchType: String?,
    val Type: String?,
    val SubType: String?,
    val Status: String,
    val Level: Int,
    val LevelNames: String,
    val Data: String,
    val LastUpdate: String,
    val Favorite: Int
)