package com.krzysztofsroga.librehome.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.database.SwitchesRoomDatabase
import com.krzysztofsroga.librehome.models.SwitchGroup
import com.krzysztofsroga.librehome.ui.mylists.OldSwitchGroup
import kotlinx.coroutines.launch

class SwitchGroupViewModel(application: Application) : AndroidViewModel(application) {

    private val switchGroupDao = SwitchesRoomDatabase.getDatabase(getApplication()).switchGroupDao

    val switchGroupList: List<OldSwitchGroup> = listOf(
        OldSwitchGroup("LEDS", "All led switches", R.drawable.led_strip),
        OldSwitchGroup("Christmas tree", "Christmas decorations", R.drawable.tree),
        OldSwitchGroup("My room", "Switches in my room", R.drawable.room),
        OldSwitchGroup("Heating", "All heating devices", R.drawable.heating)
    ) //TODO load list

    fun addGroup(switchGroup: SwitchGroup) {
        viewModelScope.launch {
            switchGroupDao.insert(switchGroup)
        }
    }

    val switchGroups: LiveData<List<SwitchGroup>>
        get() = switchGroupDao.getAllSwichGroup()

}
