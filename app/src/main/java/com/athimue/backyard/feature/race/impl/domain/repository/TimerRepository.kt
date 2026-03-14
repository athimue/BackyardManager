package com.athimue.backyard.feature.race.impl.domain.repository

import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    fun observeSeconds(): Flow<Int>
    suspend fun updateSeconds(seconds: Int)
}