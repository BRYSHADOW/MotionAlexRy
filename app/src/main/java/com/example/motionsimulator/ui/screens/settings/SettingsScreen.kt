package com.example.motionsimulator.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.motionsimulator.data.model.AppSettings
import com.example.motionsimulator.data.model.EasingType
import com.example.motionsimulator.ui.theme.ThemeColorOptions

/**
 * Man hinh Cai dat.
 * Cac thay doi duoc luu ngay lap tuc vao DataStore qua SettingsViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel:     SettingsViewModel
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("Cai dat") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lai")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier              = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement   = Arrangement.spacedBy(8.dp),
            contentPadding        = PaddingValues(vertical = 16.dp)
        ) {
            // ── Giao dien ────────────────────────────────────────────────────
            item {
                SettingsCard(title = "Giao dien") {
                    // Dark Mode
                    ToggleRow(
                        title     = "Che do toi",
                        subtitle  = "Bat che do toi toan ung dung",
                        checked   = settings.isDarkMode,
                        onToggle  = viewModel::setDarkMode
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    // Mau chu de
                    Text(
                        text  = "Mau chu de",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                    )
                    ColorPicker(
                        colors          = ThemeColorOptions,
                        selectedIndex   = settings.themeIndex,
                        onColorSelected = viewModel::setThemeIndex
                    )
                }
            }

            // ── Hoat anh mac dinh ────────────────────────────────────────────
            item {
                SettingsCard(title = "Hoat anh mac dinh") {
                    // Toc do
                    Text(
                        text  = "Toc do mac dinh: %.1fx".format(settings.animationSpeed),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Slider(
                        value         = settings.animationSpeed,
                        onValueChange = viewModel::setAnimationSpeed,
                        valueRange    = 0.1f..5.0f
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    // Delay
                    Text(
                        text  = "Delay mac dinh: %.1f giay".format(settings.delayTime),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Slider(
                        value         = settings.delayTime,
                        onValueChange = viewModel::setDelayTime,
                        valueRange    = 0f..5.0f
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    // Kieu easing
                    Text(
                        text  = "Kieu noi suy",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    EasingSelector(
                        selected = settings.easingType,
                        onSelect = viewModel::setEasingType
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    // Lap vo han
                    ToggleRow(
                        title    = "Lap vo han",
                        subtitle = "Tu dong lap lai sau khi quay ve diem goc",
                        checked  = settings.enableLoop,
                        onToggle = viewModel::setEnableLoop
                    )
                }
            }

            // ── Phan hoi cam giac ────────────────────────────────────────────
            item {
                SettingsCard(title = "Phan hoi") {
                    ToggleRow(
                        title    = "Rung",
                        subtitle = "Rung khi cham do den dich",
                        checked  = settings.vibration,
                        onToggle = viewModel::setVibration
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    ToggleRow(
                        title    = "Am thanh",
                        subtitle = "Phat am thanh beep khi den dich",
                        checked  = settings.soundEffect,
                        onToggle = viewModel::setSoundEffect
                    )
                }
            }
        }
    }
}

// ── Cac composable noi bo ─────────────────────────────────────────────────────

/** Card section co tieu de */
@Composable
private fun SettingsCard(
    title:   String,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text       = title,
                style      = MaterialTheme.typography.titleMedium,
                color      = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier   = Modifier.padding(bottom = 8.dp)
            )
            content()
        }
    }
}

/** Hang toggle co tieu de va phu de */
@Composable
private fun ToggleRow(
    title:    String,
    subtitle: String? = null,
    checked:  Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!checked) }
            .padding(vertical = 4.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Default))
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }
        }
        Switch(checked = checked, onCheckedChange = onToggle)
    }
}

/** Chon mau tu danh sach vong tron mau */
@Composable
private fun ColorPicker(
    colors:          List<Color>,
    selectedIndex:   Int,
    onColorSelected: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier              = Modifier.padding(bottom = 4.dp)
    ) {
        colors.forEachIndexed { i, color ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(
                        width  = if (i == selectedIndex) 3.dp else 0.dp,
                        color  = MaterialTheme.colorScheme.onSurface,
                        shape  = CircleShape
                    )
                    .clickable { onColorSelected(i) }
            ) {
                if (i == selectedIndex) {
                    Icon(
                        imageVector       = Icons.Default.Check,
                        contentDescription = null,
                        tint              = Color.White,
                        modifier          = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/** Chon kieu noi suy bang Segmented Button */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EasingSelector(
    selected: EasingType,
    onSelect: (EasingType) -> Unit
) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        EasingType.entries.forEachIndexed { i, type ->
            SegmentedButton(
                selected = selected == type,
                onClick  = { onSelect(type) },
                shape    = SegmentedButtonDefaults.itemShape(index = i, count = EasingType.entries.size)
            ) {
                Text(type.label, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
