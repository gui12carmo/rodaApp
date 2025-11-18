package com.example.projetoldii.ui.all

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.projetoldii.ui.all.Dark900

private val DarkColors = darkColorScheme(
    primary = PrimaryColor, onPrimary = OnPrimary,
    secondary = Primary12,  onSecondary = OnPrimary,
    tertiary = Dark900,     onTertiary = Dark800,
    background = BG_Dark,   onBackground = OnBg_Dark,
    surface = Surface_Dark, onSurface = OnSurface_Dark,
    surfaceVariant = SurfaceVariant_Dark,
    onSurfaceVariant = OnSurfaceVariant_Dark,
    error = Error,          onError = OnPrimary,
    outline = OnSurfaceVariant_Dark,
)

private val LightColors = lightColorScheme(
    primary = PrimaryColor, onPrimary = OnPrimary,
    secondary = Primary12,  onSecondary = Primary12,
    background = BG_Light,  onBackground = OnBg_Light,
    surface = Surface_Light,onSurface = OnSurface_Light,
    surfaceVariant = SurfaceVariant_Light,
    onSurfaceVariant = OnSurfaceVariant_Light,
    error = Error,          onError = OnPrimary,
    outline = OnSurfaceVariant_Light,
    tertiary = Dark900
)

@Composable
fun ProjetoLDIITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val scheme =
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        } else{
            if (darkTheme) DarkColors else LightColors
        }

    MaterialTheme(
        colorScheme = scheme,
        typography = Typography,
        content = content
    )

    }
