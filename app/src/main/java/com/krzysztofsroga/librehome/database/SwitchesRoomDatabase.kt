package com.krzysztofsroga.librehome.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteSwitch::class], version = 1)
abstract class SwitchesRoomDatabase : RoomDatabase() {
    abstract val favoriteDao: FavoriteSwitchDao
    abstract val switchGroupDao: SwitchGroupDao

    companion object {
        @Volatile
        private var INSTANCE: SwitchesRoomDatabase? = null

        fun getDatabase(context: Context): SwitchesRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SwitchesRoomDatabase::class.java,
                    "favorites_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}