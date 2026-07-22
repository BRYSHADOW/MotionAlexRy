package com.example.motionsimulator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.motionsimulator.data.model.SimulationPhase

/**
 * Panel dieu khien phia duoi man hinh.
 * Gom: hien thi toa do, thanh chinh toc do/delay, nut Start/Stop/Reset va nut Lap.
 */
@Composable
fun ControlPanel(
    redPos:    Offset,
    greenPos:  Offset,
    phase:     SimulationPhase,
    speed:     Float,
    delayTime: Float,
    loop:      Boolean,
    onSpeedChange:  (Float)   -> Unit,
    onDelayChange:  (Float)   -> Unit,
    onLoopChange:   (Boolean) -> Unit,
    onStart:        ()        -> Unit,
    onStop:         ()        -> Unit,
    onReset:        ()        -> Unit,
    modifier: Modifier = Modifier
) {
    val isIdle     = phase == SimulationPhase.IDLE
    val isRunning  = !isIdle

    Surface(
        modifier      = modifier,
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ── Toa do theo thoi gian thuc ───────────────────────────────────
            CoordinateDisplay(redPos = redPos, greenPos = greenPos)

            HorizontalDivider(
                color     = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                thickness = 1.dp
            )

            // ── Thanh chinh toc do ───────────────────────────────────────────
            SliderRow(
                label        = "Toc do",
                value        = speed,
                valueRange   = 0.1f..5.0f,
                displayValue = "%.1fx".format(speed),
                enabled      = isIdle,
                onValueChange = onSpeedChange
            )

            // ── Thanh chinh delay ────────────────────────────────────────────
            SliderRow(
                label        = "Delay",
                value        = delayTime,
                valueRange   = 0f..5.0f,
                displayValue = "%.1fs".format(delayTime),
                enabled      = isIdle,
                onValueChange = onDelayChange
            )

            HorizontalDivider(
                color     = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                thickness = 1.dp
            )

            // ── Hang nut dieu khien ──────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Nut Lap
                FilterChip(
                    selected  = loop,
                    onClick   = { onLoopChange(!loop) },
                    enabled   = isIdle,
                    label     = {
                        Text(
                            text       = if (loop) "Lap: ON" else "Lap: OFF",
                            fontWeight = FontWeight.Medium,
                            style      = MaterialTheme.typography.labelLarge
                        )
                    }
                )

                Spacer(Modifier.weight(1f))

                // Start
                Button(
                    onClick  = onStart,
                    enabled  = isIdle,
                    shape    = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(42.dp)
                ) {
                    Text("Start", fontWeight = FontWeight.Bold)
                }

                // Stop
                OutlinedButton(
                    onClick  = onStop,
                    enabled  = isRunning,
                    shape    = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(42.dp)
                ) {
                    Text("Stop")
                }

                // Reset
                OutlinedButton(
                    onClick  = onReset,
                    shape    = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(42.dp)
                ) {
                    Text("Reset")
                }
            }

            // ── Nhan trang thai hien tai ─────────────────────────────────────
            PhaseLabel(phase = phase)
        }
    }
}

// ── Hang slider co nhan ──────────────────────────────────────────────────────

@Composable
private fun SliderRow(
    label:        String,
    value:        Float,
    valueRange:   ClosedFloatingPointRange<Float>,
    displayValue: String,
    enabled:      Boolean,
    onValueChange: (Float) -> Unit
) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        modifier              = Modifier.fillMaxWidth()
    ) {
        Text(
            text     = label,
            style    = MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(56.dp)
        )
        Slider(
            value          = value,
            onValueChange  = onValueChange,
            valueRange     = valueRange,
            enabled        = enabled,
            modifier       = Modifier.weight(1f)
        )
        Text(
            text     = displayValue,
            style    = MaterialTheme.typography.labelLarge,
            color    = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(48.dp),
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ── Nhan trang thai mo phong ──────────────────────────────────────────────────

@Composable
private fun PhaseLabel(phase: SimulationPhase) {
    val (text, color) = when (phase) {
        SimulationPhase.IDLE             -> "San sang" to MaterialTheme.colorScheme.outline
        SimulationPhase.MOVING_TO_TARGET -> "Dang di chuyen den dich..." to MaterialTheme.colorScheme.primary
        SimulationPhase.WAITING          -> "Dang cho..." to MaterialTheme.colorScheme.tertiary
        SimulationPhase.RETURNING        -> "Dang quay ve..." to MaterialTheme.colorScheme.secondary
    }
    Text(
        text     = text,
        style    = MaterialTheme.typography.labelSmall,
        color    = color,
        modifier = Modifier.fillMaxWidth()
    )
}
