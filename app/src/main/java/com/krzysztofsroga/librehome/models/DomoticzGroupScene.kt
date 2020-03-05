package com.krzysztofsroga.librehome.models

import com.krzysztofsroga.librehome.connection.OnlineSwitches

class DomoticzGroupScene(
    val Name: String,
    val idx: Int,
    val Type: String?,
    val Status: String,
    val Favorite: Int
) {
    fun toLhGroupScene(): LhGroupScene {
        return when(Type) {
            "Group" -> LhGroupScene.LhGroup(Name, idx, Status != "Off")
            "Scene" -> LhGroupScene.LhScene(Name, idx)
            else -> LhGroupScene.LhUnsupportedGroupScene(Name, idx, Type)
        }
    }
}

data class DomoticzGroups(
    val result: List<DomoticzGroupScene>
) {
    fun toGroupStatesModel(): OnlineSwitches.GroupStatesModel {
        return OnlineSwitches.GroupStatesModel("Domoticz Groups", result.map { it.toLhGroupScene() })
    }
}