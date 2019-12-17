package com.krzysztofsroga.librehome.ui.switches

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.krzysztofsroga.librehome.connection.OnlineSwitches
import kotlinx.coroutines.launch

class SwitchesViewModel(application: Application) : AndroidViewModel(application) {

    private val onlineSwitches: OnlineSwitches =
        OnlineSwitches() //TODO move to viewmodel

    private val _switches = MutableLiveData<List<LightSwitch>>()

    val switches : LiveData<List<LightSwitch>>
        get() = _switches

    private val _favoriteIds = MutableLiveData<List<Int>>()

    val favoriteIds: LiveData<List<FavoriteSwitch>> = FavoriteRoomDatabase.getDatabase(getApplication()).favoriteDao().getAllFavorites()

    init {
        onlineSwitches.initialize()
        onlineSwitches.getAllSwitches { downloadedSwitches: List<LightSwitch> ->
            Log.d("initialization", "callback list")
            _switches.postValue(downloadedSwitches)
        }
    }

    fun sendSwitchState(switch: LightSwitch) {
        onlineSwitches.sendSwitchState(switch)
    }

    fun addFavorite(switch: LightSwitch) {
        viewModelScope.launch {
            FavoriteRoomDatabase.getDatabase(getApplication()).favoriteDao().insert(FavoriteSwitch(switch.id!!)) //TODO id null safety
        }
    }

}
