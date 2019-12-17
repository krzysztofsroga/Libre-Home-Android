package com.krzysztofsroga.librehome.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.krzysztofsroga.librehome.database.SwitchesRoomDatabase
import com.krzysztofsroga.librehome.models.SwitchGroup
import kotlinx.coroutines.launch

class SwitchGroupViewModel(application: Application) : AndroidViewModel(application) {

    private val switchGroupDao = SwitchesRoomDatabase.getDatabase(getApplication()).switchGroupDao

    fun addGroup(switchGroup: SwitchGroup) {
        viewModelScope.launch {
            switchGroupDao.insert(switchGroup)
        }
    }

    fun deleteGroup(switchGroup: SwitchGroup) {
        viewModelScope.launch {
            switchGroupDao.delete(switchGroup)
        }
    }

    val switchGroups: LiveData<List<SwitchGroup>>
        get() = switchGroupDao.getAllSwichGroup()

}
