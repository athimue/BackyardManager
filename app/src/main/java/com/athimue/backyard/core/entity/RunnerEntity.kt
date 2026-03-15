package com.athimue.backyard.core.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runners")
data class RunnerEntity(
    @PrimaryKey val dossardId: Int,
    val firstName: String
)