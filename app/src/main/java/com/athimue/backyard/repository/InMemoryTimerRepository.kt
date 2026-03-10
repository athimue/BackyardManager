package com.athimue.backyard.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryTimerRepository @Inject constructor() : TimerRepository {

    private val _seconds = MutableStateFlow(0)

    override fun observeSeconds(): Flow<Int> = _seconds.asStateFlow()

    override suspend fun updateSeconds(seconds: Int) {
        _seconds.value = seconds
    }
}
