package com.krzysztofsroga.librehome.ui.mylists

import com.krzysztofsroga.librehome.ui.switches.LightSwitch

data class SwitchGroup(val name: String, val description: String, val image: Int, val switchesIndices: List<LightSwitch> = listOf()) //TODO or just list of indices?
