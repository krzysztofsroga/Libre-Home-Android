package com.krzysztofsroga.librehome.ui.switches

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "favorites")
data class FavoriteSwitch(@PrimaryKey val id: Int)

@Dao interface FavoriteDao {
    @Query("SELECT * from favorites")
    suspend fun getAllFavorites(): List<FavoriteSwitch>

    @Update
    suspend fun update(favoriteSwitch: FavoriteSwitch)

    @Delete
    suspend fun delete(favoriteSwitch: FavoriteSwitch)
}

@Database(entities = [FavoriteSwitch::class], version = 1)
abstract class FavoriteRoomDatabase: RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRoomDatabase? = null

        fun getDatabase(context: Context): FavoriteRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteRoomDatabase::class.java,
                    "favorites_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}