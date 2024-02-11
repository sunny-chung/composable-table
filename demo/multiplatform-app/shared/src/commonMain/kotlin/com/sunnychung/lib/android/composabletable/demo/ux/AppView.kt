package com.sunnychung.lib.android.composabletable.demo.ux

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sunnychung.lib.android.composabletable.demo.repository.RouteSearchHttpRepository
import com.sunnychung.lib.android.composabletable.demo.model.TransitConnect
import com.sunnychung.lib.android.composabletable.demo.util.SDuration
import com.sunnychung.lib.android.composabletable.ux.Table

@Composable
fun AppView() {

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
    fun ContentCell(text: String, isChecked: Boolean, alignment: Alignment = Alignment.Center) {
        Box(modifier = Modifier
            .background(
                color = if (!isChecked) Color.White else Color.Yellow,
                shape = RoundedCornerShape(corner = CornerSize(0.dp))
            )
            .border(width = 1.dp, color = Color.Gray)
            .clickable {
                // do something wonderful
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

    val result: List<TransitConnect> by remember {
        mutableStateOf(RouteSearchHttpRepository().searchRoutes())
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
                .fillMaxSize()
                .padding(10.dp),
            columnCount = headers.size,
            rowCount = result.size + 1,
            stickyRowCount = 2,
            stickyColumnCount = 2,
            maxCellWidthDp = 320.dp
        ) { rowIndex, columnIndex ->
            val header = headers[columnIndex]
            if (rowIndex == 0) {
                HeaderCell(header)
            } else {
                val r = result[rowIndex - 1]
                when (header) {
                    "" -> ToggleCell(rowIndex - 1)
                    "#" -> ContentCell(rowIndex.toString(), checkedRows.containsKey(rowIndex - 1))
                    "Start Time" -> ContentCell(r.summary.startAt.formatHalfHoursMins(), checkedRows.containsKey(rowIndex - 1))
                    "End Time" -> ContentCell(r.summary.endAt.formatHalfHoursMins(), checkedRows.containsKey(rowIndex - 1))
                    "Duration" -> ContentCell(r.summary.duration().formatHoursMins(), checkedRows.containsKey(rowIndex - 1))
                    "#Trips" -> ContentCell(r.summary.numOfTrips.toString(), checkedRows.containsKey(rowIndex - 1))
                    "Total Fare" -> ContentCell(r.summary.totalFare.toString(), checkedRows.containsKey(rowIndex - 1))
                    "Fare (JR)" -> ContentCell(r.summary.fares["JR"]?.toString() ?: "-", checkedRows.containsKey(rowIndex - 1))
                    "Fare (Bus)" -> ContentCell(r.summary.fares["Bus"]?.toString() ?: "-", checkedRows.containsKey(rowIndex - 1))
                    "Fare (Others)" -> ContentCell(r.summary.fares["Others"]?.toString() ?: "-", checkedRows.containsKey(rowIndex - 1))
                    "Walking" -> ContentCell(SDuration(totalMs = toMillis(r.summary.walkingSeconds)).formatHoursMins(), checkedRows.containsKey(rowIndex - 1))
                    "Waiting" -> ContentCell(SDuration(totalMs = toMillis(r.summary.waitingSeconds)).formatHoursMins(), checkedRows.containsKey(rowIndex - 1))
                    "Stops" -> ContentCell(r.keyStopsFormatted(), checkedRows.containsKey(rowIndex - 1), Alignment.CenterStart)
                }
            }
        }
    }
}

