package com.example.motionsimulator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/**
 * Hien thi toa do X/Y cua hai cham theo thoi gian thuc.
 * Cap nhat moi khi StateFlow phat gia tri moi (60 FPS trong luc hoat anh).
 */
@Composable
fun CoordinateDisplay(
    redPos:   Offset,
    greenPos: Offset,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        DotCoord(
            label = "Cham Do",
            color = Color(0xFFF44336),
            pos   = redPos
        )

        // Duong ngan phan cach
        Text(
            text  = "|",
            color = MaterialTheme.colorScheme.outline,
            fontSize = 18.sp
        )

        DotCoord(
            label = "Cham Xanh",
            color = Color(0xFF4CAF50),
            pos   = greenPos
        )
    }
}

@Composable
private fun DotCoord(
    label: String,
    color: Color,
    pos:   Offset
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text  = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text  = "X: ${pos.x.roundToInt()}  Y: ${pos.y.roundToInt()}",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = FontFamily.Monospace,
                fontSize   = 13.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
