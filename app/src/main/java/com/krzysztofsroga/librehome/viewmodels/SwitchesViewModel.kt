package com.krzysztofsroga.librehome.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.krzysztofsroga.librehome.AppConfig
import com.krzysztofsroga.librehome.connection.InternetConfiguration
import com.krzysztofsroga.librehome.connection.OnlineSwitches
import com.krzysztofsroga.librehome.models.FavoriteSwitch
import com.krzysztofsroga.librehome.models.LightSwitch
import com.krzysztofsroga.librehome.models.RecentSwitch
import com.krzysztofsroga.librehome.utils.Event
import com.krzysztofsroga.librehome.utils.prefs
import com.krzysztofsroga.librehome.utils.switchesDb
import kotlinx.coroutines.launch
import java.util.*


class SwitchesViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteDao = switchesDb.favoriteDao

    private val recentDao = switchesDb.recentDao

    private val onlineSwitches: OnlineSwitches by lazy {
        val hostname = prefs.getString(AppConfig.PrefKeys.HOST, InternetConfiguration.defaultDomoticzHostname)!!
        OnlineSwitches(hostname)
    }

    private val _error = MutableLiveData<Event<Exception>>()

    val error: LiveData<Event<Exception>> = _error

    private val _switches = MutableLiveData<List<LightSwitch>>()

    val switches: LiveData<List<LightSwitch>> = _switches

    private val favoriteIds: LiveData<List<FavoriteSwitch>> = favoriteDao.getAllFavorites()

    val favoriteSwitches = Transformations.switchMap(switches) { allSwitches ->
        Transformations.map(favoriteIds) { favIds ->
            allSwitches.filter { it.id in favIds.map { it.id } }
        }
    }

    fun updateSwitches() {
        viewModelScope.launch {
            Log.d("switches", "refreshing switches")
            try {
                _switches.postValue(onlineSwitches.suspendGetAllSwitches().run {
                    val sortMode = prefs.getString(AppConfig.PrefKeys.SORTING, "domoticz")
                    when (sortMode) {
                        "alphabetically" -> sortedBy { it.name }
//                        "enabled" -> sortedByDescending { it.enabled }
                        "enabled" -> sortedWith(compareBy({ !it.enabled }, { it.name }))
                        "recent" -> {
                            val recent = recentDao.getRecentSwitches()
                            sortedWith(
                                compareByDescending<LightSwitch> { sw ->
                                    recent.find { it.id == sw.id }?.lastAccessDate?.time
                                }.thenBy { it.name }
                            )
                        }
                        else -> this
                    }
                })
            } catch (e: Exception) {
                _error.postValue(Event(e))
            }
        }
    }

    fun sendSwitchState(switch: LightSwitch) {
        viewModelScope.launch {
            recentDao.insert(RecentSwitch(switch.id!!, Calendar.getInstance().time))
            try {
                onlineSwitches.suspendSendSwitchState(switch)
            } catch (e: Exception) {
                _error.postValue(Event(e))
            }
        }
    }

    fun addFavorite(switch: LightSwitch) {
        viewModelScope.launch {
            favoriteDao.insert(FavoriteSwitch(switch.id!!))
        }
    }

    fun removeFavorite(switch: LightSwitch) {
        viewModelScope.launch {
            favoriteDao.delete(FavoriteSwitch(switch.id!!))
        }
    }

}
