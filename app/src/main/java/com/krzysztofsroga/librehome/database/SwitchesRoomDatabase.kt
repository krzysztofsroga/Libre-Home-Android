package com.krzysztofsroga.librehome.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.krzysztofsroga.librehome.models.FavoriteSwitch
import com.krzysztofsroga.librehome.models.SwitchGroup

@Database(entities = [FavoriteSwitch::class, SwitchGroup::class], version = 2)
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
                ).fallbackToDestructiveMigration().build() //TODO when stable version, don't fallback to destructive mgration
                INSTANCE = instance
                return instance
            }
        }
    }
}