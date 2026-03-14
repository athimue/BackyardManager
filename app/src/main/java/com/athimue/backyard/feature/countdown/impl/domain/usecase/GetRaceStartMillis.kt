package com.athimue.backyard.feature.countdown.impl.domain.usecase

import com.athimue.backyard.feature.race.impl.domain.repository.RaceRepository
import javax.inject.Inject

class GetRaceStartMillis @Inject constructor(
    private val raceRepository: RaceRepository
) {
    suspend operator fun invoke(): Long {
        return raceRepository.getRaceStartMillis()
    }
}