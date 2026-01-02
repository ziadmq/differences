package com.mobix.differences.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobix.differences.data.model.Level
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class GameState(
    val found: Set<String> = emptySet(),
    val mistakes: Int = 0,
    val hintRegionId: String? = null,
    val timeLeftSec: Int = 0,
    val isFinished: Boolean = false
)

class GameViewModel : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private var timerJob: Job? = null

    fun start(level: Level) {
        _state.value = GameState(timeLeftSec = level.timeLimitSec)
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                val s = _state.value
                if (s.isFinished) break
                val next = (s.timeLeftSec - 1).coerceAtLeast(0)
                _state.value = s.copy(timeLeftSec = next)
                if (next == 0) {
                    _state.value = _state.value.copy(isFinished = true)
                    break
                }
            }
        }
    }

    fun onTap(level: Level, nx: Float, ny: Float) {
        val s = _state.value
        if (s.isFinished) return

        val hit = level.regions.firstOrNull { it.id !in s.found && it.contains(nx, ny) }
        if (hit != null) {
            val newFound = s.found + hit.id
            _state.value = s.copy(
                found = newFound,
                hintRegionId = null,
                isFinished = newFound.size == level.regions.size
            )
        } else {
            val newMistakes = s.mistakes + 1
            _state.value = s.copy(
                mistakes = newMistakes,
                hintRegionId = null,
                isFinished = newMistakes >= level.maxMistakes
            )
        }
    }

    fun hint(level: Level) {
        val s = _state.value
        if (s.isFinished) return
        val notFound = level.regions.firstOrNull { it.id !in s.found } ?: return
        _state.value = s.copy(hintRegionId = notFound.id)
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}
