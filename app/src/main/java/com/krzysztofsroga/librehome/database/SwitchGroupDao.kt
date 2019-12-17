package com.krzysztofsroga.librehome.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "switch_groups")
data class SwitchGroup(@PrimaryKey(autoGenerate = true) val id: Int, val name: String, val description: String, val imagePath: String, val switchesIndices: List<Int>)

@Dao
interface SwitchGroupDao {
    @Query("SELECT * from switch_groups")
    fun getAllSwichGroup(): LiveData<List<SwitchGroup>>

    @Insert
    suspend fun insert(switchGroup: SwitchGroup)

    @Delete
    suspend fun delete(switchGroup: SwitchGroup)
}