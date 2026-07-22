package com.example.motionsimulator.ui.navigation

/** Dinh nghia route cho Navigation Graph */
sealed class Screen(val route: String) {
    object Simulation : Screen("simulation")
    object Settings   : Screen("settings")
}
