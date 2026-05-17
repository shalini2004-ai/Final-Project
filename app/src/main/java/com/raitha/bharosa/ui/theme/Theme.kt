package com.raitha.bharosa.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Agricultural Green Palette
val FarmGreen = Color(0xFF2E7D32)
val FarmGreenLight = Color(0xFF4CAF50)
val FarmGreenDark = Color(0xFF1B5E20)
val EarthBrown = Color(0xFF6D4C41)
val GoldenYellow = Color(0xFFF9A825)
val SkyBlue = Color(0xFF0288D1)
val SoilDark = Color(0xFF3E2723)
val LeafGreen = Color(0xFF66BB6A)
val SunflowerYellow = Color(0xFFFFD54F)
val WaterBlue = Color(0xFF4FC3F7)
val ErrorRed = Color(0xFFD32F2F)

private val LightColorScheme = lightColorScheme(
    primary = FarmGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA5D6A7),
    onPrimaryContainer = FarmGreenDark,
    secondary = EarthBrown,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD7CCC8),
    onSecondaryContainer = SoilDark,
    tertiary = GoldenYellow,
    onTertiary = SoilDark,
    tertiaryContainer = Color(0xFFFFF9C4),
    onTertiaryContainer = Color(0xFF4A3900),
    background = Color(0xFFF9FBF9),
    onBackground = Color(0xFF1A1C1A),
    surface = Color.White,
    onSurface = Color(0xFF1A1C1A),
    surfaceVariant = Color(0xFFE8F5E9),
    onSurfaceVariant = Color(0xFF3D4D3D),
    error = ErrorRed,
    outline = Color(0xFF72796F)
)

@Composable
fun RaithaBharosaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
