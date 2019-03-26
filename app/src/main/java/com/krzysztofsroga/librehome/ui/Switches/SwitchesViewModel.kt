package com.krzysztofsroga.librehome.ui.Switches

import androidx.lifecycle.ViewModel;
import com.krzysztofsroga.librehome.Switch

class SwitchesViewModel : ViewModel() {
    val switches = listOf<Switch>(
        Switch("kitchen", false),
        Switch("dining room", true),
        Switch("bedroom", false),
        Switch("bathroom", false)
    )
}
