package com.mobix.colortap.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobix.colortap.data.ScoreDataStore
import com.mobix.colortap.game.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val store = ScoreDataStore(application)
    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private var timerJob: Job? = null
    private var targetJob: Job? = null
    private var physicsJob: Job? = null

    init {
        viewModelScope.launch {
            store.bestScoreFlow.collect { best -> _state.update { it.copy(bestScore = best) } }
        }
    }

    fun setPlayAreaSize(w: Int, h: Int) {
        _state.update { it.copy(areaWidthPx = w, areaHeightPx = h) }
        if (_state.value.isRunning && !_state.value.targetVisible) spawnNewTarget()
    }

    fun startGame() {
        stopJobs()
        _state.update { GameState(isRunning = true, bestScore = it.bestScore, areaWidthPx = it.areaWidthPx, areaHeightPx = it.areaHeightPx) }
        generateNewMission()
        spawnNewTarget()
        startTimer()
        startPhysicsLoop()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                if (_state.value.showLevelUp || _state.value.isTimeFrozen) continue
                if (_state.value.timeLeft <= 1) { endGame(); break }
                _state.update { it.copy(timeLeft = it.timeLeft - 1) }
            }
        }
    }

    fun onTargetTap(x: Float, y: Float) {
        val s = _state.value
        if (!s.isRunning || !s.targetVisible || s.showLevelUp) return

        createExplosion(x + 100f, y + 100f, Color(s.targetColorArgb))

        if (s.missionProgress < s.missionGoal) {
            _state.update { it.copy(missionProgress = it.missionProgress + 1) }
            if (_state.value.missionProgress >= s.missionGoal) addEffect(x, y, "MISSION COMPLETE!", Color.Yellow)
        }

        when (s.targetType) {
            TargetType.TRAP -> {
                triggerShake()
                addEffect(x, y, "-5s", Color.Red)
                _state.update { it.copy(timeLeft = (it.timeLeft - 5).coerceAtLeast(0), combo = 0, targetVisible = false) }
            }
            TargetType.POWER_UP -> {
                handlePowerUp(s.powerUpType, x, y)
                _state.update { it.copy(targetVisible = false) }
            }
            else -> {
                val points = if (s.isFeverMode) 3 else 1
                addEffect(x, y, "+$points", if (s.isFeverMode) Color.Magenta else Color.Cyan)
                _state.update { it.copy(
                    score = it.score + points,
                    scoreInLevel = it.scoreInLevel + 1,
                    combo = it.combo + 1,
                    isFeverMode = it.combo + 1 >= GameConfig.FEVER_COMBO_THRESHOLD,
                    targetVisible = false
                ) }
            }
        }
        checkLevelProgress()
    }

    private fun handlePowerUp(type: PowerUpType, x: Float, y: Float) {
        when (type) {
            PowerUpType.FREEZE -> {
                addEffect(x, y, "TIME FROZEN!", Color.Cyan)
                viewModelScope.launch {
                    _state.update { it.copy(isTimeFrozen = true) }
                    delay(5000)
                    _state.update { it.copy(isTimeFrozen = false) }
                }
            }
            PowerUpType.MULTIPLIER -> {
                addEffect(x, y, "SCORE x5!", Color.Magenta)
                _state.update { it.copy(score = it.score + 5) }
            }
            else -> {}
        }
    }

    private fun generateNewMission() {
        val goal = listOf(10, 15, 20).random()
        _state.update { it.copy(missionDescription = "Tap $goal targets", missionProgress = 0, missionGoal = goal) }
    }

    private fun checkLevelProgress() {
        val s = _state.value
        val levelData = GameConfig.LEVELS.getOrNull(s.currentLevel - 1)
        if (levelData != null && s.scoreInLevel >= levelData.targetScore) goToNextLevel() else spawnNewTarget()
    }

    private fun spawnNewTarget() {
        targetJob?.cancel()
        val s = _state.value
        if (!s.isRunning || s.areaWidthPx <= 0 || s.showLevelUp) return

        val rand = Random.nextFloat()
        val type = when {
            rand < GameConfig.TRAP_CHANCE -> TargetType.TRAP
            rand < GameConfig.TRAP_CHANCE + GameConfig.SPECIAL_TARGET_CHANCE -> TargetType.POWER_UP
            else -> TargetType.NORMAL
        }

        val pType = if (type == TargetType.POWER_UP) listOf(PowerUpType.FREEZE, PowerUpType.MULTIPLIER).random() else PowerUpType.NONE

        _state.update { it.copy(
            targetVisible = true, targetType = type, powerUpType = pType,
            targetX = Random.nextInt((s.areaWidthPx - 200).coerceAtLeast(1)).toFloat(),
            targetY = Random.nextInt((s.areaHeightPx - 200).coerceAtLeast(1)).toFloat(),
            targetColorArgb = if (type == TargetType.TRAP) 0xFFFF3B30 else if (type == TargetType.POWER_UP) 0xFF00FFFF else listOf(0xFF4CAF50, 0xFF2196F3, 0xFFFFC107).random()
        ) }

        targetJob = viewModelScope.launch {
            delay(GameConfig.LEVELS[s.currentLevel - 1].targetLifeMs)
            if (_state.value.targetVisible) { _state.update { it.copy(combo = 0, targetVisible = false) }; spawnNewTarget() }
        }
    }

    private fun startPhysicsLoop() {
        physicsJob = viewModelScope.launch {
            while (isActive) {
                delay(16)
                _state.update { s -> s.copy(particles = s.particles.map { it.copy(x = it.x + it.vx, y = it.y + it.vy, vy = it.vy + 0.5f, alpha = it.alpha - 0.02f) }.filter { it.alpha > 0 }) }
            }
        }
    }

    private fun goToNextLevel() {
        viewModelScope.launch {
            _state.update { it.copy(showLevelUp = true, targetVisible = false) }
            delay(1500)
            _state.update { it.copy(showLevelUp = false, currentLevel = it.currentLevel + 1, scoreInLevel = 0) }
            generateNewMission()
            spawnNewTarget()
        }
    }

    private fun triggerShake() { viewModelScope.launch { _state.update { it.copy(isScreenShaking = true) }; delay(200); _state.update { it.copy(isScreenShaking = false) } } }
    private fun addEffect(x: Float, y: Float, text: String, color: Color) {
        val effect = TapEffect(System.currentTimeMillis(), x, y, text, color)
        _state.update { it.copy(effects = it.effects + effect) }
        viewModelScope.launch { delay(700); _state.update { it.copy(effects = it.effects - effect) } }
    }
    private fun createExplosion(x: Float, y: Float, color: Color) {
        _state.update { s -> s.copy(particles = s.particles + (1..10).map { Particle(Random.nextLong(), x, y, Random.nextFloat() * 20 - 10, Random.nextFloat() * 20 - 10, color) }) }
    }

    fun endGame() {
        _state.update { it.copy(isRunning = false, isGameOver = true) }
        stopJobs()
        viewModelScope.launch {
            if (_state.value.score > _state.value.bestScore) store.saveBestScore(_state.value.score)
        }
    }

    private fun stopJobs() {
        timerJob?.cancel()
        targetJob?.cancel()
        physicsJob?.cancel()
    }
}