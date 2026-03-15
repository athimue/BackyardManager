package com.athimue.backyard.feature.race.impl.data.repository

import com.athimue.backyard.feature.race.impl.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerRepositoryImpl @Inject constructor() : TimerRepository {

    private val _seconds = MutableStateFlow(0)

    override fun observeSeconds(): Flow<Int> = _seconds.asStateFlow()

    override suspend fun updateSeconds(seconds: Int) {
        _seconds.value = seconds
    }
}