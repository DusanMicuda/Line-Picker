package com.micudasoftware.linepicker.composeui.theme

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class AppBarShape : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(0f,0f)
            addRect(Rect(0f,0f,size.width, size.height * 0.6f))
            moveTo(0f,0f)
            addOval(Rect(0f, size.height * 0.2f, size.width, size.height))
            close()
        }
        return Outline.Generic(path)
    }


}