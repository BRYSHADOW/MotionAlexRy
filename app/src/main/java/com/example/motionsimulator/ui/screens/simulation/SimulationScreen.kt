package com.example.motionsimulator.ui.screens.simulation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.motionsimulator.ui.components.ControlPanel
import com.example.motionsimulator.ui.components.SimulationCanvas
import com.example.motionsimulator.ui.navigation.Screen
import com.example.motionsimulator.ui.screens.settings.SettingsViewModel
import com.example.motionsimulator.utils.SoundUtils
import com.example.motionsimulator.utils.VibrationUtils

/**
 * Man hinh chinh cua ung dung.
 * Layout:
 *   TopAppBar (tieu de + nut Settings)
 *   SimulationCanvas  (weight = 1f, chiem phan lon man hinh)
 *   ControlPanel      (co dinh phia duoi)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulationScreen(
    navController:    NavController,
    settingsViewModel: SettingsViewModel
) {
    val context = LocalContext.current

    // ViewModel mo phong – scoped toi man hinh nay
    val vm: SimulationViewModel = viewModel()

    // Doc trang thai tu hai ViewModel
    val settings      by settingsViewModel.settings.collectAsStateWithLifecycle()
    val redPos        by vm.redPos.collectAsStateWithLifecycle()
    val greenPos      by vm.greenPos.collectAsStateWithLifecycle()
    val phase         by vm.phase.collectAsStateWithLifecycle()
    val draggingRed   by vm.draggingRed.collectAsStateWithLifecycle()
    val draggingGreen by vm.draggingGreen.collectAsStateWithLifecycle()
    val speed         by vm.speed.collectAsStateWithLifecycle()
    val delayTime     by vm.delayTime.collectAsStateWithLifecycle()
    val loop          by vm.loop.collectAsStateWithLifecycle()

    // Dong bo kieu easing tu Settings vao SimulationViewModel
    LaunchedEffect(settings.easingType) {
        vm.setEasingType(settings.easingType)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("Motion Simulator") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(
                            imageVector       = Icons.Default.Settings,
                            contentDescription = "Mo cai dat"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor         = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor      = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Vung mo phong – chiem toan bo khong gian con lai
            SimulationCanvas(
                redPos              = redPos,
                greenPos            = greenPos,
                isDraggingRed       = draggingRed,
                isDraggingGreen     = draggingGreen,
                isSimulating        = !vm.isIdle,
                onCanvasSizeChanged = vm::onCanvasSizeChanged,
                onRedDragStart      = vm::onRedDragStart,
                onRedDrag           = vm::onRedDrag,
                onRedDragEnd        = vm::onRedDragEnd,
                onGreenDragStart    = vm::onGreenDragStart,
                onGreenDrag         = vm::onGreenDrag,
                onGreenDragEnd      = vm::onGreenDragEnd,
                modifier            = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // Panel dieu khien phia duoi
            ControlPanel(
                redPos        = redPos,
                greenPos      = greenPos,
                phase         = phase,
                speed         = speed,
                delayTime     = delayTime,
                loop          = loop,
                onSpeedChange = vm::setSpeed,
                onDelayChange = vm::setDelayTime,
                onLoopChange  = vm::setLoop,
                onStart = {
                    vm.start(
                        onVibrate = { if (settings.vibration)  VibrationUtils.vibrate(context) },
                        onSound   = { if (settings.soundEffect) SoundUtils.playBeep() }
                    )
                },
                onStop  = vm::stop,
                onReset = vm::reset,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
