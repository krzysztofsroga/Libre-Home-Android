package com.krzysztofsroga.librehome.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.krzysztofsroga.librehome.AppConfig
import com.krzysztofsroga.librehome.connection.InternetConfiguration
import com.krzysztofsroga.librehome.connection.OnlineSwitches
import com.krzysztofsroga.librehome.models.*
import com.krzysztofsroga.librehome.utils.Event
import com.krzysztofsroga.librehome.utils.prefs
import com.krzysztofsroga.librehome.utils.switchesDb
import kotlinx.coroutines.launch
import java.text.Collator
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

    private val _switches = MutableLiveData<List<LhDevice>>()

    val switches: LiveData<List<LhDevice>> = _switches

    private val _groupScenes = MutableLiveData<List<LhGroupScene>>()

    val groupScenes: LiveData<List<LhGroupScene>> = _groupScenes

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
                _switches.postValue(onlineSwitches.getAllDevices().sorted().filtered())
            } catch (e: Exception) {
                _error.postValue(Event(e))
            }
        }
    }

    fun updateScenes() {
        viewModelScope.launch {
            Log.d("scenes", "refreshing scenes")
            try {
                _groupScenes.postValue(onlineSwitches.getAllGroupScenes().sorted().filtered())
            } catch (e: Exception) {
                _error.postValue(Event(e))
            }
        }
    }

    private suspend fun <T> List<T>.sorted(): List<T> where T : LhComponent {
        return when (prefs.getString(AppConfig.PrefKeys.SORTING, "domoticz")) {
            "alphabetically" -> sortedWith(compareBy(Collator.getInstance()) { it.name })
            "enabled" -> sortedWith(compareBy({ !(it is LhComponent.Switchable && it.enabled) }, { it.name }))
            "recent" -> {
                val recent = recentDao.getRecentSwitches()
                sortedWith(
                    compareByDescending<T> { sw ->
                        recent.find { it.id == sw.id }?.lastAccessDate?.time
                    }.thenBy { it.name }
                )
            }
            else -> this
        }
    }

    private fun <T> List<T>.filtered(): List<T> where T : LhComponent {
        return if (prefs.getBoolean(AppConfig.PrefKeys.SHOW_UNSUPPORTED, true)) {
            this
        } else {
            filter { it !is LhComponent.Unsupported }
        }
    }


    fun sendComponentState(component: LhComponent) {
        viewModelScope.launch {
            recentDao.insert(RecentSwitch(component.id, Calendar.getInstance().time))
            try {
                onlineSwitches.sendComponentState(component)
            } catch (e: Exception) {
                _error.postValue(Event(e))
            }
        }
    }

    fun addFavorite(switch: LhComponent) {
        if (switch is LhDevice) { //TODO create fav list for LhGroupScene
            viewModelScope.launch {
                favoriteDao.insert(FavoriteSwitch(switch.id))
            }
        }
    }

    fun removeFavorite(switch: LhComponent) {
        if (switch is LhDevice) { //TODO create fav list for LhGroupScene
            viewModelScope.launch {
                favoriteDao.delete(FavoriteSwitch(switch.id))
            }
        }
    }

}
