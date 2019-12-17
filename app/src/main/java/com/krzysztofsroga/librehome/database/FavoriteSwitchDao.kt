package com.krzysztofsroga.librehome.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.krzysztofsroga.librehome.models.FavoriteSwitch

@Dao
interface FavoriteSwitchDao {
    @Query("SELECT * from favorites")
    fun getAllFavorites(): LiveData<List<FavoriteSwitch>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteSwitch: FavoriteSwitch)

    @Delete
    suspend fun delete(favoriteSwitch: FavoriteSwitch)
}

