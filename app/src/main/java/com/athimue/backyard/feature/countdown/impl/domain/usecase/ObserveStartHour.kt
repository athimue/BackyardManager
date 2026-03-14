package com.athimue.backyard.feature.countdown.impl.domain.usecase

import com.athimue.backyard.feature.countdown.api.model.RaceState
import com.athimue.backyard.repository.RaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveStartHour @Inject constructor(
    private val raceRepository: RaceRepository
) {
    operator fun invoke(): Flow<Int> {
        return raceRepository.observeStartHour()
    }
}