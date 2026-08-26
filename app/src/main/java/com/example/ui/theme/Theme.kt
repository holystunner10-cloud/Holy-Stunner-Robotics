package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HolyStunnerColorScheme = darkColorScheme(
    primary = CyberGold,
    onPrimary = Color(0xFF1A1000),
    primaryContainer = Color(0xFF3D2800),
    onPrimaryContainer = CyberGoldBright,
    
    secondary = CyberCyan,
    onSecondary = Color(0xFF00201C),
    secondaryContainer = Color(0xFF003831),
    onSecondaryContainer = CyberCyan,
    
    tertiary = CyberPink,
    onTertiary = Color(0xFF330018),
    tertiaryContainer = Color(0xFF550029),
    onTertiaryContainer = Color(0xFFFFB0D2),
    
    background = DarkBackground,
    onBackground = TextPrimary,
    
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    
    outline = DarkSurfaceBorder,
    outlineVariant = DarkSurfaceHighlight,
    
    error = DangerRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Holy Stunner Robotics uses a bespoke immersive cyberpunk dark command-center theme
    MaterialTheme(
        colorScheme = HolyStunnerColorScheme,
        typography = Typography,
        content = content
    )
}
