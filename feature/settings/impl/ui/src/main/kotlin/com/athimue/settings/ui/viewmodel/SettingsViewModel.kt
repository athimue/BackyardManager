package com.athimue.settings.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athimue.backyard.feature.race.api.model.RaceState
import com.athimue.settings.ui.model.SettingsRunnerUiModel
import com.athimue.settings.ui.model.SettingsUiState
import com.athimue.backyard.feature.race.impl.domain.repository.RaceRepository
import com.athimue.backyard.feature.race.impl.domain.repository.ResultsRepository
import com.athimue.settings.ui.model.toSettingsUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val raceRepository: RaceRepository,
    private val resultsRepository: ResultsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _resetDone = MutableStateFlow(false)
    val resetDone: StateFlow<Boolean> = _resetDone.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                raceRepository.observeRaceState(),
                raceRepository.observeStartHour(),
                raceRepository.observeStartMinute()
            ) { state, hour, minute -> Triple(state, hour, minute) }
                .combine(resultsRepository.observeRunners()) { (state, hour, minute), runners ->
                    SettingsUiState(
                        startHour = hour,
                        startMinute = minute,
                        runners = runners.map { it.toSettingsUiModel() },
                        raceState = state
                    )
                }.collect { _uiState.value = it }
        }
    }

    fun incrementStartHour() {
        val newHour = (_uiState.value.startHour + 1) % 24
        viewModelScope.launch { raceRepository.setStartTime(newHour, _uiState.value.startMinute) }
    }

    fun decrementStartHour() {
        val newHour = (_uiState.value.startHour - 1 + 24) % 24
        viewModelScope.launch { raceRepository.setStartTime(newHour, _uiState.value.startMinute) }
    }

    fun incrementStartMinute() {
        val newMinute = (_uiState.value.startMinute + 5) % 60
        viewModelScope.launch { raceRepository.setStartTime(_uiState.value.startHour, newMinute) }
    }

    fun decrementStartMinute() {
        val newMinute = (_uiState.value.startMinute - 5 + 60) % 60
        viewModelScope.launch { raceRepository.setStartTime(_uiState.value.startHour, newMinute) }
    }

    fun removeRunner(runnerId: Int) {
        viewModelScope.launch { resultsRepository.removeRunner(runnerId) }
    }

    fun restoreDefaultRunners() {
        viewModelScope.launch { resultsRepository.restoreDefaultRunners() }
    }

    fun resetRace() {
        viewModelScope.launch {
            resultsRepository.clearAllResults()
            raceRepository.setActualStartMillis(0L)
            raceRepository.setRaceState(RaceState.COUNTDOWN)
            _resetDone.update { true }
        }
    }

    fun onResetDoneConsumed() {
        _resetDone.update { false }
    }
}