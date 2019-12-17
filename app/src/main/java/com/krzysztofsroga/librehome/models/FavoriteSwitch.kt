package com.krzysztofsroga.librehome.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorites")
data class FavoriteSwitch(@PrimaryKey val id: Int)