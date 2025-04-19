package io.cricket.bookcricketapp.utils

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Composable function that creates Text with scaled font size
 */
@Composable
fun ScaledText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    style: TextStyle = LocalTextStyle.current
) {
    val scale = rememberUiScaleUtils()
    
    // Scale font size only if provided, otherwise use the style's font size
    val scaledFontSize = if (fontSize != TextUnit.Unspecified) {
        (scale.scaledSp(fontSize.value)).sp
    } else if (style.fontSize != TextUnit.Unspecified) {
        (scale.scaledSp(style.fontSize.value)).sp
    } else {
        TextUnit.Unspecified
    }
    
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = scaledFontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        maxLines = maxLines,
        overflow = overflow,
        style = style
    )
}

/**
 * Composable function for scaled headline text
 */
@Composable
fun ScaledHeadlineSmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    ScaledText(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

/**
 * Composable function for scaled headline medium text
 */
@Composable
fun ScaledHeadlineMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    ScaledText(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.headlineMedium,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

/**
 * Composable function for scaled title text
 */
@Composable
fun ScaledTitleMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    ScaledText(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

/**
 * Composable function for scaled title large text
 */
@Composable
fun ScaledTitleLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    ScaledText(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

/**
 * Composable function for scaled title small text
 */
@Composable
fun ScaledTitleSmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    ScaledText(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleSmall,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

/**
 * Composable function for scaled body text
 */
@Composable
fun ScaledBodyMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    ScaledText(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        lineHeight = lineHeight,
        maxLines = maxLines,
        overflow = overflow
    )
}

/**
 * Composable function for scaled body large text
 */
@Composable
fun ScaledBodyLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    ScaledText(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

/**
 * Composable function for scaled label large text
 */
@Composable
fun ScaledLabelLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    ScaledText(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelLarge,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

/**
 * Composable function for scaled label medium text
 */
@Composable
fun ScaledLabelMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    ScaledText(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelMedium,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

/**
 * Function to scale any specific SP value
 */
@Composable
fun scaledSp(value: Float): TextUnit {
    val scale = rememberUiScaleUtils()
    return (scale.scaledSp(value)).sp
}