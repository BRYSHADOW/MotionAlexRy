package com.example.motionsimulator.ui.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.motionsimulator.data.model.AppSettings
import com.example.motionsimulator.data.model.EasingType
import com.example.motionsimulator.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel cho man hinh Settings.
 * Dung AndroidViewModel de lay Application context cho SettingsRepository.
 * Duoc tao o Activity scope (trong MainActivity) va chia se giua
 * SimulationScreen (doc) va SettingsScreen (doc + ghi).
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository(application)

    // Cache settings hien tai de cac man hinh doc nhanh
    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    init {
        // Thu thap Flow tu DataStore, cap nhat _settings khi co thay doi
        viewModelScope.launch {
            repository.settingsFlow.collect { s -> _settings.value = s }
        }
    }

    // ── Cac ham cap nhat cai dat ─────────────────────────────────────────────
    fun setDarkMode(v: Boolean)      = viewModelScope.launch { repository.setDarkMode(v) }
    fun setThemeIndex(v: Int)        = viewModelScope.launch { repository.setThemeIndex(v) }
    fun setAnimationSpeed(v: Float)  = viewModelScope.launch { repository.setAnimationSpeed(v) }
    fun setDelayTime(v: Float)       = viewModelScope.launch { repository.setDelayTime(v) }
    fun setEnableLoop(v: Boolean)    = viewModelScope.launch { repository.setEnableLoop(v) }
    fun setVibration(v: Boolean)     = viewModelScope.launch { repository.setVibration(v) }
    fun setSoundEffect(v: Boolean)   = viewModelScope.launch { repository.setSoundEffect(v) }
    fun setEasingType(v: EasingType) = viewModelScope.launch { repository.setEasingType(v) }
}
