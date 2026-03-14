package com.athimue.backyard.feature.countdown.impl.domain.usecase

import com.athimue.backyard.feature.countdown.api.model.RaceState
import com.athimue.backyard.repository.RaceRepository
import javax.inject.Inject

class SetRaceState @Inject constructor(
    private val raceRepository: RaceRepository
) {
    suspend operator fun invoke(raceState: RaceState) {
        return raceRepository.setRaceState(raceState)
    }
}