package com.krzysztofsroga.librehome.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.krzysztofsroga.librehome.models.RecentSwitch

@Dao
interface RecentSwitchesDao {
    @Query("SELECT * FROM recent_switches")
    suspend fun getRecentSwitches(): List<RecentSwitch>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recentSwitch: RecentSwitch)

    @Delete
    suspend fun delete(recentSwitch: RecentSwitch)
}