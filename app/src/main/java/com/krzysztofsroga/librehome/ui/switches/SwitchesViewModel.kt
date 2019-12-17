package com.krzysztofsroga.librehome.ui.switches

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.krzysztofsroga.librehome.connection.OnlineSwitches
import kotlinx.coroutines.launch


class SwitchesViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteDao = FavoriteRoomDatabase.getDatabase(getApplication()).favoriteDao()

    private val onlineSwitches: OnlineSwitches = OnlineSwitches()

    private val _switches = MutableLiveData<List<LightSwitch>>()

    val switches: LiveData<List<LightSwitch>>
        get() = _switches

    private val favoriteIds: LiveData<List<FavoriteSwitch>> = favoriteDao.getAllFavorites()

    val favoriteSwitches = Transformations.switchMap(switches) { allSwitches ->
        Transformations.map(favoriteIds) { favIds ->
            allSwitches.filter { it.id in favIds.map { it.id } }
        }
    }

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
            favoriteDao.insert(FavoriteSwitch(switch.id!!)) //TODO id null safety
        }
    }

    fun removeFavorite(switch: LightSwitch) {
        viewModelScope.launch {
            favoriteDao.delete(FavoriteSwitch(switch.id!!)) //TODO id null safety
        }
    }

}
