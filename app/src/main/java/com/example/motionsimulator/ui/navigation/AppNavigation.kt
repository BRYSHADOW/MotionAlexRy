package com.example.motionsimulator.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.motionsimulator.ui.screens.settings.SettingsScreen
import com.example.motionsimulator.ui.screens.settings.SettingsViewModel
import com.example.motionsimulator.ui.screens.simulation.SimulationScreen

/**
 * NavHost chinh cua ung dung.
 * settingsViewModel duoc tao o Activity scope (trong MainActivity)
 * va truyen xuong de chia se giua SimulationScreen va SettingsScreen.
 */
@Composable
fun AppNavigation(settingsViewModel: SettingsViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController  = navController,
        startDestination = Screen.Simulation.route
    ) {
        composable(Screen.Simulation.route) {
            SimulationScreen(
                navController    = navController,
                settingsViewModel = settingsViewModel
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                navController = navController,
                viewModel     = settingsViewModel
            )
        }
    }
}
