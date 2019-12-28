package com.krzysztofsroga.librehome.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.krzysztofsroga.librehome.AppConfig
import com.krzysztofsroga.librehome.connection.InternetConfiguration
import com.krzysztofsroga.librehome.database.SwitchesRoomDatabase
import com.krzysztofsroga.librehome.connection.OnlineSwitches
import com.krzysztofsroga.librehome.models.FavoriteSwitch
import com.krzysztofsroga.librehome.models.LightSwitch
import kotlinx.coroutines.launch


class SwitchesViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteDao = SwitchesRoomDatabase.getDatabase(getApplication()).favoriteDao

    private val onlineSwitches: OnlineSwitches by lazy {
        val prefs = PreferenceManager.getDefaultSharedPreferences(getApplication())
        val hostname = prefs.getString(AppConfig.PrefKeys.HOST, InternetConfiguration.defaultDomoticzHostname)!!
        OnlineSwitches(hostname)
    }

    private val _error = MutableLiveData<String>()

    val error : LiveData<String> = _error

    private val _switches = MutableLiveData<List<LightSwitch>>()

    val switches: LiveData<List<LightSwitch>> = _switches

    private val favoriteIds: LiveData<List<FavoriteSwitch>> = favoriteDao.getAllFavorites()

    val favoriteSwitches = Transformations.switchMap(switches) { allSwitches ->
        Transformations.map(favoriteIds) { favIds ->
            allSwitches.filter { it.id in favIds.map { it.id } }
        }
    }

    fun updateSwitches() {
        onlineSwitches.getAllSwitches { downloadedSwitches: List<LightSwitch> ->
            Log.d("switches", "refreshing switches")
            _switches.postValue(downloadedSwitches)
        }
    }

    fun sendSwitchState(switch: LightSwitch) {
        onlineSwitches.sendSwitchState(switch)
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
