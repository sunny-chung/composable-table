package com.sunnychung.lib.android.composabletable.demo.ux

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.sunnychung.lib.android.composabletable.mpdemo.R

actual suspend fun loadContentFont(): FontFamily {
    return FontFamily(Font(R.font.notosansjp_regular))
}
