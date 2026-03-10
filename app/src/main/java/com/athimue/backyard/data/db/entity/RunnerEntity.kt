package com.athimue.backyard.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runners")
data class RunnerEntity(
    @PrimaryKey val id: Int,
    val firstName: String
)
