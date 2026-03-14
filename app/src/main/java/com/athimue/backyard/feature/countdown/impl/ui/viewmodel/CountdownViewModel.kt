package com.athimue.backyard.feature.countdown.impl.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athimue.backyard.feature.countdown.api.model.RaceState
import com.athimue.backyard.feature.countdown.impl.domain.usecase.GetRaceStartMillis
import com.athimue.backyard.feature.countdown.impl.domain.usecase.ObserveRaceState
import com.athimue.backyard.feature.countdown.impl.domain.usecase.ObserveStartHour
import com.athimue.backyard.feature.countdown.impl.domain.usecase.ObserveStartMinute
import com.athimue.backyard.feature.countdown.impl.domain.usecase.SetActualStartMillis
import com.athimue.backyard.feature.countdown.impl.domain.usecase.SetRaceState
import com.athimue.backyard.feature.countdown.impl.ui.model.CountdownUiState
import com.athimue.backyard.feature.countdown.impl.ui.model.RaceStateUiModel
import com.athimue.backyard.feature.countdown.impl.ui.model.toRaceStateUiModel
import com.athimue.backyard.feature.timer.ui.model.formatCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountdownViewModel @Inject constructor(
    private val observeRaceState: ObserveRaceState,
    private val observeStartHour: ObserveStartHour,
    private val observeStartMinute: ObserveStartMinute,
    private val getRaceStartMillis: GetRaceStartMillis,
    private val setRaceState: SetRaceState,
    private val setActualStartMillis: SetActualStartMillis,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CountdownUiState())
    val uiState: StateFlow<CountdownUiState> = _uiState.asStateFlow()

    init {
        observeRaceConfig()
        startCountdownTicker()
    }

    private fun observeRaceConfig() {
        viewModelScope.launch {
            combine(
                observeRaceState.invoke(),
                observeStartHour.invoke(),
                observeStartMinute.invoke()
            ) { state, hour, minute -> Triple(state, hour, minute) }
                .collect { (state, hour, minute) ->
                    _uiState.update { it.copy(raceState = state.toRaceStateUiModel(), startHour = hour, startMinute = minute) }
                }
        }
    }

    private fun startCountdownTicker() {
        viewModelScope.launch {
            while (isActive) {
                val scheduledMillis = getRaceStartMillis.invoke()
                val now = System.currentTimeMillis()
                val diffSeconds = (scheduledMillis - now) / 1000

                // Auto-start only if the scheduled time passed less than 5 minutes ago.
                // A large negative diffSeconds means the configured time is way in the past
                // (e.g. after a race reset mid-day), which must not trigger an immediate re-start.
                if (diffSeconds in -300L..0L && _uiState.value.raceState == RaceStateUiModel.COUNTDOWN) {
                    setActualStartMillis.invoke(scheduledMillis)
                    setRaceState.invoke(RaceState.IN_PROGRESS)
                }

                _uiState.update {
                    it.copy(
                        countdownSeconds = maxOf(0L, diffSeconds),
                        currentTime = formatCurrentTime()
                    )
                }
                delay(1_000)
            }
        }
    }

    fun startRaceNow() {
        viewModelScope.launch {
            // Manual start: record the exact moment the button was pressed.
            setActualStartMillis.invoke(System.currentTimeMillis())
            setRaceState.invoke(RaceState.IN_PROGRESS)
        }
    }
}