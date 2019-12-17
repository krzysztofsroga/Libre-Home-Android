package com.krzysztofsroga.librehome.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "favorites")
data class FavoriteSwitch(@PrimaryKey val id: Int)

@Dao
interface FavoriteSwitchDao {
    @Query("SELECT * from favorites")
    fun getAllFavorites(): LiveData<List<FavoriteSwitch>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteSwitch: FavoriteSwitch)

    @Delete
    suspend fun delete(favoriteSwitch: FavoriteSwitch)
}

