package com.sunnychung.lib.android.composabletable.ux

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp

@Composable
fun Table(
    modifier: Modifier = Modifier,
    columnCount: Int,
    rowCount: Int,
    maxCellWidthDp: Dp = Dp.Infinity,
    maxCellHeightDp: Dp = Dp.Infinity,
    verticalScrollState: ScrollState = rememberScrollState(),
    horizontalScrollState: ScrollState = rememberScrollState(),
    cellContent: @Composable (columnIndex: Int, rowIndex: Int) -> Unit
) {
    val columnWidths = remember { mutableStateMapOf<Int, Int>() }
    val rowHeights = remember { mutableStateMapOf<Int, Int>() }

    val maxCellWidth = if (listOf(Dp.Infinity, Dp.Unspecified).contains(maxCellWidthDp)) {
        Constraints.Infinity
    } else {
        with(LocalDensity.current) { maxCellWidthDp.toPx() }.toInt()
    }
    val maxCellHeight = if (listOf(Dp.Infinity, Dp.Unspecified).contains(maxCellHeightDp)) {
        Constraints.Infinity
    } else {
        with(LocalDensity.current) { maxCellHeightDp.toPx() }.toInt()
    }

    Box(
        modifier = modifier
            .then(Modifier.horizontalScroll(horizontalScrollState))
            .then(Modifier.verticalScroll(verticalScrollState))
    ) {
        Layout(
            content = {

                (0 until rowCount).forEach { rowIndex ->
                    (0 until columnCount).forEach { columnIndex ->
                        cellContent(columnIndex, rowIndex)
                    }
                }
            },
        ) { measurables, constraints ->
            val placeables = measurables.mapIndexed { index, it ->
                val columnIndex = index % columnCount
                val rowIndex = index / columnCount
                it.measure(Constraints(
                    minWidth = columnWidths[columnIndex] ?: 0,
                    minHeight = rowHeights[rowIndex] ?: 0,
                    maxWidth = maxCellWidth,
                    maxHeight = maxCellHeight
                ))
            }

            placeables.forEachIndexed { index, placeable ->
                val columnIndex = index % columnCount
                val rowIndex = index / columnCount

                val existingWidth = columnWidths[columnIndex] ?: 0
                val maxWidth = maxOf(existingWidth, placeable.width)
                if (maxWidth > existingWidth) {
                    columnWidths[columnIndex] = maxWidth
                }

                val existingHeight = rowHeights[rowIndex] ?: 0
                val maxHeight = maxOf(existingHeight, placeable.height)
                if (maxHeight > existingHeight) {
                    rowHeights[rowIndex] = maxHeight
                }
            }

            val accumWidths = mutableListOf(0)
            (1..columnWidths.size).forEach { i ->
                accumWidths += accumWidths.last() + columnWidths[i-1]!!
            }
            val accumHeights = mutableListOf(0)
            (1..rowHeights.size).forEach { i ->
                accumHeights += accumHeights.last() + rowHeights[i-1]!!
            }

            val totalWidth = accumWidths.last()
            val totalHeight = accumHeights.last()

            layout(width = totalWidth, height = totalHeight) {
                placeables.forEachIndexed { index, placeable ->
                    val columnIndex = index % columnCount
                    val rowIndex = index / columnCount

                    placeable.placeRelative(accumWidths[columnIndex], accumHeights[rowIndex])
                }
            }
        }
    }
}
