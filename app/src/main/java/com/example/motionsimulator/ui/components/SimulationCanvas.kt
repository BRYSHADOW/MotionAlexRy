package com.example.motionsimulator.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.example.motionsimulator.ui.screens.simulation.SimulationViewModel
import com.example.motionsimulator.ui.theme.GreenDot
import com.example.motionsimulator.ui.theme.RedDot

/**
 * Canvas mo phong toan man hinh.
 *
 * Trach nhiem:
 *  - Ve hai cham co hieu ung phat sang / bong do / vien
 *  - Ve duong ket noi net dut
 *  - Xu ly cham / keo tha bang awaitEachGesture
 *
 * Cac su kien keo duoc bao len ViewModel de xu ly.
 */
@Composable
fun SimulationCanvas(
    redPos:   Offset,
    greenPos: Offset,
    isDraggingRed:   Boolean,
    isDraggingGreen: Boolean,
    isSimulating:    Boolean,
    onCanvasSizeChanged: (Size)   -> Unit,
    onRedDragStart:      ()       -> Unit,
    onRedDrag:           (Offset) -> Unit,
    onRedDragEnd:        ()       -> Unit,
    onGreenDragStart:    ()       -> Unit,
    onGreenDrag:         (Offset) -> Unit,
    onGreenDragEnd:      ()       -> Unit,
    modifier: Modifier = Modifier
) {
    val dotRadius    = SimulationViewModel.DOT_RADIUS
    val touchRadius  = dotRadius * 2.2f     // vung cham rong hon de de keo

    // Thu thap mau truoc (khong the goi MaterialTheme ben trong Canvas draw)
    val bgColor      = MaterialTheme.colorScheme.surfaceVariant
    val gridColor    = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
    val lineColor    = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)

    Canvas(
        modifier = modifier
            .background(bgColor)
            .onSizeChanged { sz ->
                onCanvasSizeChanged(Size(sz.width.toFloat(), sz.height.toFloat()))
            }
            .pointerInput(isSimulating) {
                awaitEachGesture {
                    // Cho cam ung dau tien
                    val down     = awaitFirstDown(requireUnconsumed = false)
                    val touchPos = down.position

                    // Xac dinh cham nao duoc cham vao
                    val distRed   = (touchPos - redPos).getDistance()
                    val distGreen = (touchPos - greenPos).getDistance()

                    val target = when {
                        !isSimulating && distRed   <= touchRadius -> "red"
                        distGreen <= touchRadius                  -> "green"
                        else                                      -> null
                    }

                    if (target == null) return@awaitEachGesture

                    down.consume()
                    when (target) {
                        "red"   -> onRedDragStart()
                        "green" -> onGreenDragStart()
                    }

                    // Theo doi chuyen dong ngon tay
                    while (true) {
                        val event   = awaitPointerEvent()
                        val pointer = event.changes.firstOrNull { it.id == down.id }
                            ?: break
                        if (!pointer.pressed) break
                        if (pointer.positionChanged()) {
                            val delta = pointer.positionChange()
                            when (target) {
                                "red"   -> onRedDrag(delta)
                                "green" -> onGreenDrag(delta)
                            }
                            pointer.consume()
                        }
                    }

                    // Ket thuc keo
                    when (target) {
                        "red"   -> onRedDragEnd()
                        "green" -> onGreenDragEnd()
                    }
                }
            }
    ) {
        // 1. Luoi nen
        drawGrid(gridColor)

        // 2. Duong ket noi net dut (chi hien khi ca hai cham da duoc khoi tao)
        if (redPos != Offset.Zero && greenPos != Offset.Zero) {
            drawLine(
                color       = lineColor,
                start       = redPos,
                end         = greenPos,
                strokeWidth = 2.dp.toPx(),
                pathEffect  = PathEffect.dashPathEffect(floatArrayOf(18f, 10f))
            )
        }

        // 3. Ve cham xanh (dich) truoc
        if (greenPos != Offset.Zero) {
            drawGlowDot(greenPos, GreenDot, dotRadius, isDraggingGreen)
        }

        // 4. Ve cham do (nguon) sau de hien len tren
        if (redPos != Offset.Zero) {
            drawGlowDot(redPos, RedDot, dotRadius, isDraggingRed)
        }
    }
}

// ── Ham ve cham co phat sang ──────────────────────────────────────────────────

/**
 * Ve mot cham co:
 *  - Hieu ung glow (nhieu vong tron mo dan ra ngoai)
 *  - Bong do (dich xuong duoi)
 *  - Vong tron chinh (solid color)
 *  - Diem sang (white highlight goc tren trai)
 *  - Vien trang mong
 *  - Phong to 20% khi dang keo
 */
private fun DrawScope.drawGlowDot(
    position:  Offset,
    color:     Color,
    radius:    Float,
    isPressed: Boolean
) {
    val r = if (isPressed) radius * 1.2f else radius

    // Lop glow (ngoai vao trong)
    drawCircle(color = color.copy(alpha = 0.06f), radius = r * 4.0f, center = position)
    drawCircle(color = color.copy(alpha = 0.12f), radius = r * 2.8f, center = position)
    drawCircle(color = color.copy(alpha = 0.20f), radius = r * 2.0f, center = position)
    drawCircle(color = color.copy(alpha = 0.30f), radius = r * 1.5f, center = position)

    // Bong do (dich xuong 4dp)
    drawCircle(
        color  = Color.Black.copy(alpha = 0.28f),
        radius = r,
        center = position + Offset(0f, 4.dp.toPx())
    )

    // Vong tron chinh
    drawCircle(color = color, radius = r, center = position)

    // Diem sang phan quang (top-left)
    drawCircle(
        color  = Color.White.copy(alpha = 0.40f),
        radius = r * 0.38f,
        center = position - Offset(r * 0.28f, r * 0.28f)
    )

    // Vien trang mong
    drawCircle(
        color  = Color.White.copy(alpha = 0.55f),
        radius = r,
        center = position,
        style  = Stroke(width = 2.5.dp.toPx())
    )
}

// ── Luoi nen ──────────────────────────────────────────────────────────────────

private fun DrawScope.drawGrid(color: Color) {
    val spacing = 48.dp.toPx()
    val cols    = (size.width  / spacing).toInt() + 2
    val rows    = (size.height / spacing).toInt() + 2
    val sw      = 1f

    for (i in 0..cols) {
        drawLine(color, Offset(i * spacing, 0f), Offset(i * spacing, size.height), sw)
    }
    for (j in 0..rows) {
        drawLine(color, Offset(0f, j * spacing), Offset(size.width, j * spacing), sw)
    }
}
