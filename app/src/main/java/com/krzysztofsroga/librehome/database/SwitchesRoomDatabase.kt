package com.krzysztofsroga.librehome.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.krzysztofsroga.librehome.models.FavoriteSwitch
import com.krzysztofsroga.librehome.models.RecentSwitch
import com.krzysztofsroga.librehome.models.SwitchGroup

@Database(entities = [FavoriteSwitch::class, SwitchGroup::class, RecentSwitch::class], exportSchema = true, version = 4)
@TypeConverters(Converters::class)
abstract class SwitchesRoomDatabase : RoomDatabase() {
    abstract val favoriteDao: FavoriteSwitchDao
    abstract val switchGroupDao: SwitchGroupDao
    abstract val recentDao: RecentSwitchesDao

    companion object {
        @Volatile
        private var INSTANCE: SwitchesRoomDatabase? = null

        private val MIGRATION_2_3 = object : Migration(2, 3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE switch_groups ADD COLUMN switchesIndices TEXT NOT NULL DEFAULT ''")
            }
        }
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `recent_switches` (`id` INTEGER NOT NULL, `lastAccessDate` INTEGER NOT NULL, PRIMARY KEY(`id`))")
            }
        }

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
                ).addMigrations(MIGRATION_2_3, MIGRATION_3_4).fallbackToDestructiveMigrationOnDowngrade().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}