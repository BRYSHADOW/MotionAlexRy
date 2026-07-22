package com.example.motionsimulator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Theme chinh cua ung dung.
 *
 * @param darkTheme   Bat/tat Dark Mode (lay tu Settings)
 * @param themeIndex  Chi so bo mau (0-4) trong ThemePresets
 */
@Composable
fun MotionSimulatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeIndex: Int = 0,
    content: @Composable () -> Unit
) {
    val preset      = ThemePresets.getOrElse(themeIndex) { ThemePresets[0] }
    val colorScheme = if (darkTheme) preset.second else preset.first

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
