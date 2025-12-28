package com.negativespace.puzzler.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negativespace.puzzler.data.repository.SettingsRepository
import com.negativespace.puzzler.domain.model.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    val settings: StateFlow<UserSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettings())
    
    fun setReducedPrecision(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setReducedPrecision(enabled)
        }
    }
    
    fun setAlternativeInput(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setAlternativeInput(enabled)
        }
    }
    
    fun setHighContrast(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setHighContrast(enabled)
        }
    }
    
    fun setHapticFeedback(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setHapticFeedback(enabled)
        }
    }
    
    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateSettings(settings.value.withSound(enabled))
        }
    }
    
    fun setShowAds(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setShowAds(enabled)
        }
    }
}
