package com.example.motionsimulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.motionsimulator.ui.navigation.AppNavigation
import com.example.motionsimulator.ui.screens.settings.SettingsViewModel
import com.example.motionsimulator.ui.theme.MotionSimulatorTheme

/**
 * Activity duy nhat – Entry point cua ung dung.
 *
 * Trach nhiem:
 *  1. Khoi tao SettingsViewModel o scope Activity (chia se giua tat ca man hinh)
 *  2. Doc settings va truyen vao MotionSimulatorTheme de ap dung Dark Mode / mau chu de
 *  3. Render AppNavigation
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // SettingsViewModel scope Activity – dung chung cho tat ca man hinh
            val settingsViewModel: SettingsViewModel = viewModel()
            val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

            MotionSimulatorTheme(
                darkTheme  = settings.isDarkMode,
                themeIndex = settings.themeIndex
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(settingsViewModel = settingsViewModel)
                }
            }
        }
    }
}
