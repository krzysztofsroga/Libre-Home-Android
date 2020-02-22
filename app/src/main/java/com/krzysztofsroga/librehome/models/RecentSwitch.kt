package com.krzysztofsroga.librehome.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "recent_switches")
data class RecentSwitch(@PrimaryKey val id: Int, val lastAccessDate: Date)