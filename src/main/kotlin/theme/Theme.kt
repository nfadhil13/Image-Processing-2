package theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkColorPalette = darkColors(
    primary = primary700,
    primaryVariant = primary300,
    secondary = primary900,
    onPrimary = Color.White,
    background = Color.Black,
    surface = Color.Black,
    error = Color(176, 0, 32),
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.White

)

val LightColorPalette = lightColors(
    primary = primary700,
    primaryVariant = primary300,
    surface = Color.White,
    background = Color.White,
    error = Color(176, 0, 32),
    onPrimary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

@Composable
fun MyTheme(darkTheme: Boolean = false, content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        shapes = shapes,
        content = content
    )
}