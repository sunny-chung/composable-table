package com.sunnychung.lib.android.composabletable.demo.ux

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import composabletable.demo.`multiplatform-app`.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
actual suspend fun loadContentFont(): FontFamily {
    val contentFontBytes = Res.readBytes("font/notosansjp_regular.ttf")
    return FontFamily(Font(identity = "JP-Regular", data = contentFontBytes))
}
