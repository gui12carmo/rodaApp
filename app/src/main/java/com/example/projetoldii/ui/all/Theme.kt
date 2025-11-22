package com.example.projetoldii.ui.all

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetoldii.ui.all.Dark900

private val DarkColors = darkColorScheme(
    primary = PrimaryColor,                 onPrimary = Light0,
    primaryContainer = Light0,
    secondary = Primary12,
    secondaryContainer = PrimaryColor,      onSecondaryContainer = Light0,
    tertiaryContainer = Light50,            onTertiaryContainer = Dark800,
    background = BG_Dark,
    surface = Light0,
    surfaceVariant = Light50 ,
    onSurfaceVariant = Light200,
    error = Error,                          onError = Light0,
    outline = Grey900,
)

private val LightColors = lightColorScheme(
    primary = PrimaryColor,                 onPrimary = Light0,
    primaryContainer = Light0,              onPrimaryContainer = Dark900,
    secondary = Primary12,                  onSecondary = Mid500,
    secondaryContainer = PrimaryColor,      onSecondaryContainer = Light0,
    tertiaryContainer = Light50,            onTertiaryContainer = Dark800,
    background = BG_Light,
    surface = Light0,
    surfaceVariant = Light50 ,
    onSurfaceVariant = Light200,
    error = Error,                          onError = Light0,
    outline = Grey900,
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

@Composable
fun Swatches() {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(Modifier.size(60.dp).background(MaterialTheme.colorScheme.primary))
        Box(Modifier.size(60.dp).background(MaterialTheme.colorScheme.secondary))
        Box(Modifier.size(60.dp).background(MaterialTheme.colorScheme.primaryContainer))
        Box(Modifier.size(60.dp).background(MaterialTheme.colorScheme.onPrimaryContainer))
        Box(Modifier.size(60.dp).background(MaterialTheme.colorScheme.secondaryContainer))
        Box(Modifier.size(60.dp).background(MaterialTheme.colorScheme.onSecondaryContainer))
        Box(Modifier.size(60.dp).background(MaterialTheme.colorScheme.tertiaryContainer))
        Box(Modifier.size(60.dp).background(MaterialTheme.colorScheme.onTertiaryContainer))
        Box(Modifier.size(60.dp).background(MaterialTheme.colorScheme.surface))
        Box(Modifier.size(60.dp).background(MaterialTheme.colorScheme.surfaceVariant))
        Box(Modifier.size(60.dp).background(MaterialTheme.colorScheme.background))
        Box(Modifier.size(60.dp).background(MaterialTheme.colorScheme.error))
        Box(Modifier.size(60.dp).background(MaterialTheme.colorScheme.outline))
    }
}

@Preview
@Composable
fun PreviewSwatches() {
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) { Swatches() }
}


