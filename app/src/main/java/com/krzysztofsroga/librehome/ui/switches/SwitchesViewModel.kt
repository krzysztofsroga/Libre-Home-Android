package com.krzysztofsroga.librehome.ui.switches

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager

class SwitchesViewModel : ViewModel() {

    private val onlineSwitches: OnlineSwitches = OnlineSwitches() //TODO move to viewmodel

    private val _switches = MutableLiveData<List<LightSwitch>>()

    val switches : LiveData<List<LightSwitch>>
        get() = _switches


    fun initialize() {
        onlineSwitches.initialize()
        onlineSwitches.getAllSwitches { downloadedSwitches: List<LightSwitch> ->
            Log.d("initialization", "callback list")
            _switches.postValue(downloadedSwitches)
        }
    }

    fun sendSwitchState(switch: LightSwitch) {
        onlineSwitches.sendSwitchState(switch)
    }
//    val switches = listOf(
//        LightSwitch("kitchen", false),
//        LightSwitch("dining room", true),
//        LightSwitch("bedroom", false),
//        LightSwitch("bathroom", false)
//    )
}
