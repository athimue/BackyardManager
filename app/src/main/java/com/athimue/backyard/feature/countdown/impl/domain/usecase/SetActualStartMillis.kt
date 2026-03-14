package com.athimue.backyard.feature.countdown.impl.domain.usecase

import com.athimue.backyard.feature.race.impl.domain.repository.RaceRepository
import javax.inject.Inject

class SetActualStartMillis @Inject constructor(
    private val raceRepository: RaceRepository
) {
    suspend operator fun invoke(millis: Long) {
        return raceRepository.setActualStartMillis(millis)
    }
}