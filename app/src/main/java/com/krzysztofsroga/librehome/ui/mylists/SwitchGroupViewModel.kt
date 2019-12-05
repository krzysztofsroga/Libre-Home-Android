package com.krzysztofsroga.librehome.ui.mylists

import androidx.lifecycle.ViewModel
import com.krzysztofsroga.librehome.R

class SwitchGroupViewModel : ViewModel() {
    val switchGroupList: List<SwitchGroup> = listOf(
        SwitchGroup("LEDS", "All led switches", R.drawable.led_strip),
        SwitchGroup("Christmas tree", "Christmas decorations", R.drawable.tree),
        SwitchGroup("My room", "Switches in my room", R.drawable.room),
        SwitchGroup("Heating", "All heating devices", R.drawable.heating)
    ) //TODO load list

}
