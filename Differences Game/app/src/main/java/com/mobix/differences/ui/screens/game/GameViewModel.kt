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
    val hearts: Int = 5,
    val hintRegionId: String? = null,
    val timeLeftSec: Int = 0,
    val isFinished: Boolean = false,
    val stars: Int = 0,
    val win: Boolean = false
)

class GameViewModel : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private var timerJob: Job? = null

    fun start(level: Level) {
        _state.value = GameState(
            timeLeftSec = level.timeLimitSec,
            hearts = level.maxMistakes
        )
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                val s = _state.value
                if (s.isFinished) break

                if (s.timeLeftSec > 0) {
                    _state.value = s.copy(timeLeftSec = s.timeLeftSec - 1)
                } else {
                    finishGame(false)
                }
            }
        }
    }

    // داخل GameViewModel.kt
    fun onTap(level: Level, nx: Float, ny: Float) {
        val s = _state.value
        if (s.isFinished) return

        // البحث عن أول منطقة "غير مكتشفة" تحتوي على هذه الإحداثيات
        val hitRegion = level.regions.find { region ->
            region.id !in s.found && region.contains(nx, ny)
        }

        if (hitRegion != null) {
            // حالة النجاح: إضافة المعرف للقائمة المكتشفة
            val updatedFound = s.found + hitRegion.id
            _state.value = s.copy(
                found = updatedFound,
                isFinished = updatedFound.size == level.regions.size
            )
            // هنا يمكن تشغيل صوت "نجاح"
        } else {
            // حالة الخطأ: خصم قلب
            val remainingHearts = s.hearts - 1
            _state.value = s.copy(
                hearts = remainingHearts,
                isFinished = remainingHearts <= 0
            )
            // هنا يمكن تشغيل صوت "خطأ" واهتزاز (Haptic Feedback)
        }
    }

    private fun finishGame(win: Boolean) {
        val s = _state.value
        val stars = if (win) {
            if (s.hearts >= 4) 3 else if (s.hearts >= 2) 2 else 1
        } else 0

        _state.value = s.copy(isFinished = true, win = win, stars = stars)
        timerJob?.cancel()
    }

    fun hint(level: Level) {
        val s = _state.value
        if (s.isFinished || s.found.size == level.regions.size) return
        val notFound = level.regions.firstOrNull { it.id !in s.found } ?: return
        _state.value = s.copy(hintRegionId = notFound.id)
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}