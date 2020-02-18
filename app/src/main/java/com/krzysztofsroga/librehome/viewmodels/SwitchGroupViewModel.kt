package com.krzysztofsroga.librehome.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.krzysztofsroga.librehome.database.SwitchesRoomDatabase
import com.krzysztofsroga.librehome.models.SwitchGroup
import com.krzysztofsroga.librehome.utils.switchesDb
import kotlinx.coroutines.launch

class SwitchGroupViewModel(application: Application) : AndroidViewModel(application) {

    private val switchGroupDao = switchesDb.switchGroupDao

    val switchGroups: LiveData<List<SwitchGroup>> = switchGroupDao.getAllSwitchGroup()

    fun addGroup(switchGroup: SwitchGroup) {
        viewModelScope.launch {
            switchGroupDao.insert(switchGroup)
        }
    }

    fun updateGroup(switchGroup: SwitchGroup) {
        viewModelScope.launch {
            switchGroupDao.update(switchGroup)
            Log.d("group update", "updated $switchGroup")
        }
    }

    fun deleteGroup(switchGroup: SwitchGroup) {
        viewModelScope.launch {
            switchGroupDao.delete(switchGroup)
        }
    }

    suspend fun loadGroup(groupId: Int): SwitchGroup {
        return switchGroupDao.findGroupById(groupId)
    }
}
