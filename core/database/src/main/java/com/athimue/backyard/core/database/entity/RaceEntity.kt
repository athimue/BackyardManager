package com.athimue.backyard.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "race")
data class RaceEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "start_hour") val startHour: Int = 9,
    @ColumnInfo(name = "start_minute") val startMinute: Int = 0,
    val state: String = "COUNTDOWN",
    /** Epoch millis of the moment the race actually started. 0 = not started yet. */
    @ColumnInfo(name = "actual_start_millis") val actualStartMillis: Long = 0L
)