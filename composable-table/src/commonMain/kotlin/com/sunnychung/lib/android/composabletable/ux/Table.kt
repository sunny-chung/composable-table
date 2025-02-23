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
    rowCount: Int,
    columnCount: Int,
    stickyRowCount: Int = 0,
    stickyColumnCount: Int = 0,
    maxCellWidthDp: Dp = Dp.Infinity,
    maxCellHeightDp: Dp = Dp.Infinity,
    verticalScrollState: ScrollState = rememberScrollState(),
    horizontalScrollState: ScrollState = rememberScrollState(),
    cellContent: @Composable (rowIndex: Int, columnIndex: Int) -> Unit
) {
    val columnWidths = remember { mutableStateMapOf<Int, Int>() }
    val rowHeights = remember { mutableStateMapOf<Int, Int>() }

    if (columnCount != columnWidths.size || rowCount != rowHeights.size) {
        columnWidths.clear()
        rowHeights.clear()
    }

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

    var accumWidths = mutableListOf<Int>()
    var accumHeights = mutableListOf<Int>()

    @Composable
    fun StickyCells(modifier: Modifier = Modifier, rowCount: Int, columnCount: Int) {
        if (rowCount > 0 && columnCount > 0) {
            Box(modifier = modifier) {
                Layout(
                    content = {
                        (0 until rowCount).forEach { rowIndex ->
                            (0 until columnCount).forEach { columnIndex ->
                                cellContent(rowIndex = rowIndex, columnIndex = columnIndex)
                            }
                        }
                    },
                ) { measurables, constraints ->
                    val placeables = measurables.mapIndexed { index, it ->
                        val columnIndex = index % columnCount
                        val rowIndex = index / columnCount
                        it.measure(
                            Constraints(
                                minWidth = columnWidths[columnIndex] ?: 0,
                                minHeight = rowHeights[rowIndex] ?: 0,
                                maxWidth = columnWidths[columnIndex] ?: 0,
                                maxHeight = rowHeights[rowIndex] ?: 0
                            )
                        )
                    }

                    val totalWidth = accumWidths[columnCount]
                    val totalHeight = accumHeights[rowCount]

                    layout(width = totalWidth, height = totalHeight) {
                        placeables.forEachIndexed { index, placeable ->
                            val columnIndex = index % columnCount
                            val rowIndex = index / columnCount

                            placeable.placeRelative(
                                accumWidths[columnIndex],
                                accumHeights[rowIndex]
                            )
                        }
                    }
                }
            }
        }
    }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .then(Modifier.horizontalScroll(horizontalScrollState))
                .then(Modifier.verticalScroll(verticalScrollState))
        ) {
            Layout(
                content = {
                    (0 until rowCount).forEach { rowIndex ->
                        (0 until columnCount).forEach { columnIndex ->
                            cellContent(rowIndex = rowIndex, columnIndex = columnIndex)
                        }
                    }
                },
            ) { measurables, constraints ->
                val placeables = measurables.mapIndexed { index, it ->
                    val columnIndex = index % columnCount
                    val rowIndex = index / columnCount
                    it.measure(
                        Constraints(
                            minWidth = columnWidths[columnIndex] ?: 0,
                            minHeight = rowHeights[rowIndex] ?: 0,
                            maxWidth = maxCellWidth,
                            maxHeight = maxCellHeight
                        )
                    )
                }

                placeables.forEachIndexed { index, placeable ->
                    val columnIndex = index % columnCount
                    val rowIndex = index / columnCount

                    val existingWidth = columnWidths[columnIndex] ?: 0
                    val maxWidth = maxOf(existingWidth, placeable.width)
                    if (maxWidth > existingWidth || (existingWidth == 0 && maxWidth == existingWidth)) {
                        columnWidths[columnIndex] = maxWidth
                    }

                    val existingHeight = rowHeights[rowIndex] ?: 0
                    val maxHeight = maxOf(existingHeight, placeable.height)
                    if (maxHeight > existingHeight || (existingHeight == 0 && maxHeight == existingHeight)) {
                        rowHeights[rowIndex] = maxHeight
                    }
                }

                accumWidths = mutableListOf(0).apply {
                    (1..columnWidths.size).forEach { i ->
                        this += this.last() + columnWidths[i - 1]!!
                    }
                }
                accumHeights = mutableListOf(0).apply {
                    (1..rowHeights.size).forEach { i ->
                        this += this.last() + rowHeights[i - 1]!!
                    }
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

        if (columnWidths.isEmpty() || rowHeights.isEmpty()) {
            return@Box
        }

        StickyCells(
            modifier = Modifier.horizontalScroll(horizontalScrollState),
            rowCount = stickyRowCount,
            columnCount = columnCount
        )

        StickyCells(
            modifier = Modifier.verticalScroll(verticalScrollState),
            rowCount = rowCount,
            columnCount = stickyColumnCount
        )

        StickyCells(
            rowCount = stickyRowCount,
            columnCount = stickyColumnCount
        )
    }
}
