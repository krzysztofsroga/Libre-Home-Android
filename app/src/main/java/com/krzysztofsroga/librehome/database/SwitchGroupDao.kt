package com.krzysztofsroga.librehome.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.krzysztofsroga.librehome.models.SwitchGroup

@Dao
interface SwitchGroupDao {
    @Query("SELECT * from switch_groups")
    fun getAllSwitchGroup(): LiveData<List<SwitchGroup>>

    @Query("SELECT * from switch_groups WHERE id = :groupId")
    suspend fun findGroupById(groupId: Int): SwitchGroup

    @Insert
    suspend fun insert(switchGroup: SwitchGroup)

    @Update
    suspend fun update(switchGroup: SwitchGroup)

//    @Query("UPDATE switch_groups SET name = :switchGroup.")
//    suspend fun update(switchGroup: SwitchGroup)

    @Delete
    suspend fun delete(switchGroup: SwitchGroup)
}