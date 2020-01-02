package com.krzysztofsroga.librehome.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.krzysztofsroga.librehome.AppConfig
import com.krzysztofsroga.librehome.connection.InternetConfiguration
import com.krzysztofsroga.librehome.connection.OnlineSwitches
import com.krzysztofsroga.librehome.database.SwitchesRoomDatabase
import com.krzysztofsroga.librehome.models.FavoriteSwitch
import com.krzysztofsroga.librehome.models.LightSwitch
import com.krzysztofsroga.librehome.utils.Event
import kotlinx.coroutines.launch
import java.net.MalformedURLException


class SwitchesViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteDao = SwitchesRoomDatabase.getDatabase(getApplication()).favoriteDao

    private val onlineSwitches: OnlineSwitches by lazy {
        val prefs = PreferenceManager.getDefaultSharedPreferences(getApplication())
        val hostname = prefs.getString(AppConfig.PrefKeys.HOST, InternetConfiguration.defaultDomoticzHostname)!!
        OnlineSwitches(hostname)
    }

    private val _error = MutableLiveData<Event<String>>()

    val error: LiveData<Event<String>> = _error

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
                _switches.postValue(onlineSwitches.suspendGetAllSwitches())
            } catch (e: MalformedURLException) {
                _error.postValue(Event("Connection error: $e"))
            }
        }
    }

    fun sendSwitchState(switch: LightSwitch) {
        viewModelScope.launch {
            try {
            onlineSwitches.suspendSendSwitchState(switch)

            } catch(e: MalformedURLException) {
                _error.postValue(Event("Connection error: $e"))
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
