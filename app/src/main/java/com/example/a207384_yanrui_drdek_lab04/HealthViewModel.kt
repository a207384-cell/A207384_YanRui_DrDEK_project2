package com.example.a207384_yanrui_drdek_lab04

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// 1. 数据类：保存 UI 需要的所有状态
data class HealthUiState(
    // 基础信息
    val userName: String = "Yan Rui",
    val dailyWaterGoalLiters: Double = 2.5,

    // 加水相关
    val waterInput: String = "",
    val totalWaterLiters: Double = 1.8,
    val resultMessage: String = "Ready to track your health today",
    val waterLogs: List<Int> = emptyList(),

    // Profile 页面编辑状态 (演示数据修改)
    val profileNameInput: String = "Yan Rui",
    val profileGoalInput: String = "2.5",

    // BMI 计算器状态 (演示数据处理)
    val weightInput: String = "",
    val heightInput: String = "",
    val bmiResult: Double = 0.0
)

// 2. ViewModel：管理数据和逻辑
class HealthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HealthUiState())
    val uiState: StateFlow<HealthUiState> = _uiState.asStateFlow()

    // --- 加水逻辑 ---
    fun updateWaterInput(input: String) {
        _uiState.update { it.copy(waterInput = input) }
    }

    fun addWater() {
        val currentInput = _uiState.value.waterInput
        val addedAmount = currentInput.toIntOrNull() ?: 0
        val newTotal = _uiState.value.totalWaterLiters + (addedAmount / 1000.0)

        // 把新增加的水量加到记录列表的最前面
        val updatedLogs = listOf(addedAmount) + _uiState.value.waterLogs

        _uiState.update {
            it.copy(
                totalWaterLiters = newTotal,
                resultMessage = "✅ Water intake recorded: $currentInput ml\nYour SDG 3 health goal updated!",
                waterInput = "",
                waterLogs = updatedLogs
            )
        }
    }

    fun resetStatus() {
        _uiState.update { it.copy(resultMessage = "Ready to track your health today") }
    }

    // --- Profile 修改逻辑 (得分点：修改数据) ---
    fun updateProfileNameInput(name: String) {
        _uiState.update { it.copy(profileNameInput = name) }
    }

    fun updateProfileGoalInput(goal: String) {
        _uiState.update { it.copy(profileGoalInput = goal) }
    }

    fun saveProfile() {
        _uiState.update {
            it.copy(
                userName = it.profileNameInput,
                dailyWaterGoalLiters = it.profileGoalInput.toDoubleOrNull() ?: it.dailyWaterGoalLiters
            )
        }
    }

    // --- BMI 计算逻辑 (得分点：数据处理与展示) ---
    fun updateWeightInput(weight: String) {
        _uiState.update { it.copy(weightInput = weight) }
    }

    fun updateHeightInput(height: String) {
        _uiState.update { it.copy(heightInput = height) }
    }

    fun calculateBmi() {
        val w = _uiState.value.weightInput.toDoubleOrNull() ?: 0.0
        val hCm = _uiState.value.heightInput.toDoubleOrNull() ?: 0.0
        if (hCm > 0) {
            val hM = hCm / 100.0
            val bmi = w / (hM * hM)
            _uiState.update { it.copy(bmiResult = bmi) }
        }
    }
}