package com.krzysztofsroga.librehome.ui

import android.app.Application
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.krzysztofsroga.librehome.connection.SshConnection
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//https://stackoverflow.com/questions/14323661/simple-ssh-connect-with-jsch
class SshViewModel(application: Application) : AndroidViewModel(application) {

    private val _out: MutableLiveData<String> = MutableLiveData()

    val out: LiveData<String>
        get() = _out

    private val prefs
        get() = PreferenceManager.getDefaultSharedPreferences(getApplication())

    fun checkConnection() {
        viewModelScope.launch {
            SshConnection(prefs).checkConnection().collect { _out.value = it }
        }
    }

    fun restartRaspberry() {
        viewModelScope.launch {
            SshConnection(prefs).restartRpi().collect { _out.value = it }
        }
    }
}
