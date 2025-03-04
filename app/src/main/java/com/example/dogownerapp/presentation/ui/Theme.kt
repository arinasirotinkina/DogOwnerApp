package com.example.dogownerapp.presentation.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.dogownerapp.R

// 🎨 Определяем цветовую схему
private val CustomLightColors = lightColorScheme(
    primary = Color(0xFFA74B0F),
    secondary = Color(0xFFBE4D20),
    background = Color(0xFFFDF0E9),
    surface = Color(0xFFF8DDCB),
    onSurface = Color.Black,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
)

// 🎨 Опционально: можно добавить тёмную тему
private val CustomDarkColors = darkColorScheme(
    primary = Color(0xFFBE4D20),
    secondary = Color(0xFFA74B0F),
    background = Color(0xFF1E1E1E),
    surface = Color(0xFF2C2C2C),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

// 🌙 Функция выбора темы (светлая/тёмная)
@Composable
fun CustomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Можно подключить isSystemInDarkTheme()
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) CustomDarkColors else CustomLightColors

    MaterialTheme(
        colorScheme = colors, // Опционально: можно задать шрифты
        content = content
    )
}

