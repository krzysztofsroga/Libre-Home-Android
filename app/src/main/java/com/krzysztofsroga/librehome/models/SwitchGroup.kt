package com.krzysztofsroga.librehome.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "switch_groups")
data class SwitchGroup(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val description: String,
    val imagePath: String,
    val switchesIndices: List<Int> = listOf()
)
