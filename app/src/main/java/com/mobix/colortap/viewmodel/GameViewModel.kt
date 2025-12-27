package com.mobix.colortap.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobix.colortap.data.ScoreDataStore
import com.mobix.colortap.game.GameConfig
import com.mobix.colortap.game.GameState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.random.Random

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val store = ScoreDataStore(application)

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private var timerJob: Job? = null
    private var targetJob: Job? = null

    init {
        viewModelScope.launch {
            store.bestScoreFlow.collect { best ->
                _state.update { it.copy(bestScore = best) }
            }
        }
    }

    fun setPlayAreaSize(widthPx: Int, heightPx: Int) {
        _state.update { it.copy(areaWidthPx = widthPx, areaHeightPx = heightPx) }
    }

    fun startGame() {
        stopJobs()

        _state.update {
            it.copy(
                isRunning = true,
                isGameOver = false,
                score = 0,
                combo = 0,
                timeLeft = GameConfig.ROUND_SECONDS,
                targetLifeMs = GameConfig.START_LIFE_MS
            )
        }

        spawnNewTarget()

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val newTime = _state.value.timeLeft - 1
                if (newTime <= 0) {
                    endGame()
                    break
                } else {
                    _state.update { it.copy(timeLeft = newTime) }
                }
            }
        }

        targetJob = viewModelScope.launch {
            while (_state.value.isRunning) {
                val life = _state.value.targetLifeMs
                delay(life)

                // If target still visible and not tapped => miss
                if (_state.value.isRunning && _state.value.targetVisible) {
                    onMiss()
                    spawnNewTarget()
                }
            }
        }
    }

    fun onTargetTap() {
        val current = _state.value
        if (!current.isRunning || !current.targetVisible) return

        val newScore = current.score + 1
        val newCombo = current.combo + 1

        val shouldIncrease = (newScore % GameConfig.SCORE_STEP_TO_INCREASE_DIFFICULTY == 0)
        val newLife = if (shouldIncrease) {
            max(GameConfig.MIN_LIFE_MS, current.targetLifeMs - GameConfig.LIFE_DECREASE_MS)
        } else current.targetLifeMs

        _state.update {
            it.copy(
                score = newScore,
                combo = newCombo,
                targetVisible = false,
                targetLifeMs = newLife
            )
        }

        spawnNewTarget()
    }

    private fun onMiss() {
        // خيار: ما تنقص نقاط، بس صفّر الكومبو
        _state.update { it.copy(combo = 0) }
    }

    private fun spawnNewTarget() {
        val s = _state.value
        if (s.areaWidthPx <= 0 || s.areaHeightPx <= 0) {
            // إذا لسه ما انقاس حجم الشاشة، خلّيه ظاهر بمكان ثابت مؤقتاً
            _state.update {
                it.copy(
                    targetVisible = true,
                    targetX = 0f,
                    targetY = 0f,
                    targetColorArgb = randomColor().value.toLong(),
                    targetId = System.currentTimeMillis()
                )
            }
            return
        }

        // target size بالـ px: رح نبعت الحجم من UI كـ padding آمن تقريباً عبر targetSizePx
        // هنا بنحجز مساحة بسيطة حول الأطراف
        val padding = 24f
        val maxX = (s.areaWidthPx.toFloat() - padding).coerceAtLeast(0f)
        val maxY = (s.areaHeightPx.toFloat() - padding).coerceAtLeast(0f)

        val x = Random.nextFloat() * maxX
        val y = Random.nextFloat() * maxY

        _state.update {
            it.copy(
                targetVisible = true,
                targetX = x,
                targetY = y,
                targetColorArgb = randomColor().value.toLong(),
                targetId = System.currentTimeMillis()
            )
        }
    }

    fun endGame() {
        val finalScore = _state.value.score
        _state.update { it.copy(isRunning = false, isGameOver = true, targetVisible = false) }
        stopJobs()

        viewModelScope.launch {
            val best = _state.value.bestScore
            if (finalScore > best) {
                store.saveBestScore(finalScore)
            }
        }
    }

    private fun stopJobs() {
        timerJob?.cancel()
        targetJob?.cancel()
        timerJob = null
        targetJob = null
    }

    private fun randomColor(): Color {
        val palette = listOf(
            Color(0xFF4CAF50),
            Color(0xFF2196F3),
            Color(0xFFFFC107),
            Color(0xFFE91E63),
            Color(0xFF9C27B0),
            Color(0xFFFF5722)
        )
        return palette.random()
    }

    override fun onCleared() {
        stopJobs()
        super.onCleared()
    }
}