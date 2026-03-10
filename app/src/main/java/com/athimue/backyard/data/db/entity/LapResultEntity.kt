package com.athimue.backyard.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lap_results",
    indices = [Index(value = ["runner_id", "lap_number"], unique = true)]
)
data class LapResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "runner_id") val runnerId: Int,
    @ColumnInfo(name = "lap_number") val lapNumber: Int,
    val time: String,
    val status: String
)
