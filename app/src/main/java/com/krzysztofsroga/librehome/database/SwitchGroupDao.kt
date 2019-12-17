package com.krzysztofsroga.librehome.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.krzysztofsroga.librehome.models.SwitchGroup

@Dao
interface SwitchGroupDao {
    @Query("SELECT * from switch_groups")
    fun getAllSwichGroup(): LiveData<List<SwitchGroup>>

    @Insert
    suspend fun insert(switchGroup: SwitchGroup)

    @Delete
    suspend fun delete(switchGroup: SwitchGroup)
}