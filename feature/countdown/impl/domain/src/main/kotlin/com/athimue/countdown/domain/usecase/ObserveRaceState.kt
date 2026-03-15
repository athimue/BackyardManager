package com.athimue.backyard.feature.countdown.impl.domain.usecase

import com.athimue.backyard.feature.race.api.model.RaceState
import com.athimue.backyard.feature.race.impl.domain.repository.RaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRaceState @Inject constructor(
    private val raceRepository: RaceRepository
) {
    operator fun invoke(): Flow<RaceState> {
        return raceRepository.observeRaceState()
    }
}