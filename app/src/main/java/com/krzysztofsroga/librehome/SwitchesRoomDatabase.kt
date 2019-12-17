package com.krzysztofsroga.librehome

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.krzysztofsroga.librehome.ui.switches.FavoriteDao
import com.krzysztofsroga.librehome.ui.switches.FavoriteSwitch

@Database(entities = [FavoriteSwitch::class], version = 1)
abstract class SwitchesRoomDatabase: RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

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