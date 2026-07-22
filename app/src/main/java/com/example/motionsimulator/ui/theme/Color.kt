package com.example.motionsimulator.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/** Mau cham mo phong (dung trong Canvas drawing) */
val RedDot   = Color(0xFFF44336)
val GreenDot = Color(0xFF4CAF50)

/** Danh sach mau seed hien thi trong Settings – muc 0 la mac dinh */
val ThemeColorOptions: List<Color> = listOf(
    Color(0xFF6750A4), // Tim
    Color(0xFF0061A4), // Xanh duong
    Color(0xFF006E1C), // Xanh la
    Color(0xFFB3261E), // Do
    Color(0xFFBF360C), // Cam
)

/** 5 cap ColorScheme (light, dark) tuong ung voi ThemeColorOptions */
val ThemePresets = listOf(
    Pair(
        lightColorScheme(primary = Color(0xFF6750A4), secondary = Color(0xFF625B71), tertiary = Color(0xFF7D5260)),
        darkColorScheme( primary = Color(0xFFD0BCFF), secondary = Color(0xFFCCC2DC), tertiary = Color(0xFFEFB8C8))
    ),
    Pair(
        lightColorScheme(primary = Color(0xFF0061A4), secondary = Color(0xFF535F70), tertiary = Color(0xFF6B5778)),
        darkColorScheme( primary = Color(0xFF9ECAFF), secondary = Color(0xFFBBC7DB), tertiary = Color(0xFFD3BBDF))
    ),
    Pair(
        lightColorScheme(primary = Color(0xFF006E1C), secondary = Color(0xFF52634F), tertiary = Color(0xFF38656A)),
        darkColorScheme( primary = Color(0xFF72DC6B), secondary = Color(0xFFB9CCB4), tertiary = Color(0xFFA1CDD2))
    ),
    Pair(
        lightColorScheme(primary = Color(0xFFB3261E), secondary = Color(0xFF715B5D), tertiary = Color(0xFF7D5700)),
        darkColorScheme( primary = Color(0xFFFFB4AB), secondary = Color(0xFFE8BDBD), tertiary = Color(0xFFFFBA5C))
    ),
    Pair(
        lightColorScheme(primary = Color(0xFFBF360C), secondary = Color(0xFF77574A), tertiary = Color(0xFF6B5E2F)),
        darkColorScheme( primary = Color(0xFFFFB5A0), secondary = Color(0xFFE8BDB0), tertiary = Color(0xFFD8C68E))
    ),
)
