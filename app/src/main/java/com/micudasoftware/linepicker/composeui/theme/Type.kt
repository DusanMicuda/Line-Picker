package com.micudasoftware.linepicker.composeui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        textAlign = TextAlign.Center,
    ),

    h2 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    ),

    h3 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),

    h6 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp,
            color = Color.Red
        ),

    subtitle1 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontStyle = FontStyle.Italic,
        fontSize = 16.sp,
        color = Color.DarkGray
    )

)