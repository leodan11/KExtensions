package com.github.leodan11.k_extensions.core.content

import android.graphics.Color
import android.graphics.Typeface
import androidx.annotation.ColorInt

data class WatermarkOptions(
    val corner: Corner = Corner.BOTTOM_RIGHT,
    val textSizeToWidthRatio: Float = 0.04f,
    val paddingToWidthRatio: Float = 0.03f,
    @ColorInt val textColor: Int = Color.WHITE,
    @ColorInt val shadowColor: Int? = Color.BLACK,
    val typeface: Typeface? = null
)
