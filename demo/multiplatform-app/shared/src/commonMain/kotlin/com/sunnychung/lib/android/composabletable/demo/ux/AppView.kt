package com.sunnychung.lib.android.composabletable.demo.ux

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sunnychung.lib.android.composabletable.demo.model.TransitConnect
import com.sunnychung.lib.android.composabletable.demo.repository.RouteSearchHttpRepository
import com.sunnychung.lib.android.composabletable.demo.util.SDuration
import com.sunnychung.lib.android.composabletable.ux.Table
import kotlinx.coroutines.launch

@Composable
fun AppView() {
    // Use custom font to work around the issue that Compose WASM cannot display Unicode characters
    // https://github.com/JetBrains/compose-multiplatform/issues/3967
    val coroutineScope = rememberCoroutineScope()
    var contentFontFamily: FontFamily? by remember { mutableStateOf(null) }
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            contentFontFamily = loadContentFont()
        }
    }

    CompositionLocalProvider(LocalTextStyle provides LocalTextStyle.current.copy(fontFamily = contentFontFamily)) {
        if (contentFontFamily != null) {
            AppContentView()
        }
    }
}

@Composable
fun AppContentView() {
    @Composable
    fun HeaderCell(text: String) {
        if (text.isNullOrBlank()) {
            Surface {}
            return
        }
        Box(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(corner = CornerSize(0.dp))
                )
                .border(width = 1.dp, color = Color.Gray)
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.Center)
            )
        }
    }

    @Composable
    fun ContentCell(text: String, isChecked: Boolean, alignment: Alignment = Alignment.Center, onClick: () -> Unit = {}) {
        Box(modifier = Modifier
            .background(
                color = if (!isChecked) Color.White else Color.Yellow,
                shape = RoundedCornerShape(corner = CornerSize(0.dp))
            )
            .border(width = 1.dp, color = Color.Gray)
            .clickable {
                // do something wonderful
                onClick()
            }
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(10.dp)
                    .align(alignment)
            )
        }
    }

    val checkedRows = remember {
        mutableStateMapOf<Int, Unit>()
    }

    val result: MutableList<TransitConnect> = remember {
        mutableStateListOf(*RouteSearchHttpRepository().searchRoutes().toTypedArray())
    }

    @Composable
    fun ToggleCell(row: Int) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.background(Color.White)) {
            Switch(
                checked = checkedRows.containsKey(row),
                onCheckedChange = {
                    if (!checkedRows.containsKey(row)) {
                        checkedRows[row] = Unit
                    } else {
                        checkedRows.remove(row)
                    }
                }
            )
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Search Result (Multiplatform)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp)
        )

        val headers = listOf(
            "",
            "#",
            "Start Time",
            "End Time",
            "Duration",
            "#Trips",
            "Total Fare",
            "Fare (JR)",
            "Fare (Bus)",
            "Fare (Others)",
            "Walking",
            "Waiting",
            "Stops"
        )
        Table(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(10.dp),
            columnCount = headers.size,
            rowCount = result.size + 1,
            stickyRowCount = 1,
            stickyColumnCount = 2,
            maxCellWidthDp = 320.dp
        ) { rowIndex, columnIndex ->
            val header = headers[columnIndex]
            if (rowIndex == 0) {
                HeaderCell(header)
            } else {
                val row = rowIndex - 1
                val r = result.elementAtOrNull(row) ?: return@Table
                when (header) {
                    "" -> ToggleCell(row)
                    "#" -> ContentCell(rowIndex.toString(), checkedRows.containsKey(row))
                    "Start Time" -> ContentCell(r.summary.startAt.formatHalfHoursMins(), checkedRows.containsKey(row))
                    "End Time" -> ContentCell(r.summary.endAt.formatHalfHoursMins(), checkedRows.containsKey(row))
                    "Duration" -> ContentCell(r.summary.duration().formatHoursMins(), checkedRows.containsKey(row))
                    "#Trips" -> ContentCell(r.summary.numOfTrips.toString(), checkedRows.containsKey(row))
                    "Total Fare" -> ContentCell(r.summary.totalFare.toString(), checkedRows.containsKey(row))
                    "Fare (JR)" -> ContentCell(r.summary.fares["JR"]?.toString() ?: "-", checkedRows.containsKey(row))
                    "Fare (Bus)" -> ContentCell(r.summary.fares["Bus"]?.toString() ?: "-", checkedRows.containsKey(row))
                    "Fare (Others)" -> ContentCell(r.summary.fares["Others"]?.toString() ?: "-", checkedRows.containsKey(row))
                    "Walking" -> ContentCell(SDuration(totalMs = toMillis(r.summary.walkingSeconds)).formatHoursMins(), checkedRows.containsKey(row))
                    "Waiting" -> ContentCell(SDuration(totalMs = toMillis(r.summary.waitingSeconds)).formatHoursMins(), checkedRows.containsKey(row))
                    "Stops" -> ContentCell(r.keyStopsFormatted(), checkedRows.containsKey(row), Alignment.CenterStart) {
                        // lengthen
                        result[row].keyStops += result[row].keyStops
                        if (!checkedRows.containsKey(row)) { // force the cell to be recomposed
                            checkedRows[row] = Unit
                        } else {
                            checkedRows.remove(row)
                        }
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(onClick = {
                result.removeAt(result.lastIndex)
            }) {
                Text("Remove")
            }

            Button(onClick = {
                if (result.isNotEmpty()) {
                    result.add(result.last())
                }
            }) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(10.dp))
    }
}

