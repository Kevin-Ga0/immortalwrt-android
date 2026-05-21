package com.immortalwrt.manager.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.immortalwrt.manager.ui.screens.traffic.TrafficDataPoint

@Composable
fun TrafficGraph(dataPoints: List<TrafficDataPoint>, modifier: Modifier = Modifier) {
    val rxColor = Color(0xFF2196F3)
    val txColor = Color(0xFF4CAF50)
    val bgColor = Color(0xFF1A1A2E)

    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row { Box(Modifier.size(12.dp).background(rxColor)); Spacer(Modifier.width(4.dp)); Text("RX", style = MaterialTheme.typography.labelSmall) }
            Row { Box(Modifier.size(12.dp).background(txColor)); Spacer(Modifier.width(4.dp)); Text("TX", style = MaterialTheme.typography.labelSmall) }
        }
        Spacer(Modifier.height(4.dp))
        if (dataPoints.size >= 2) {
            Canvas(modifier = Modifier.fillMaxWidth().height(200.dp).background(bgColor, MaterialTheme.shapes.medium)) {
                val maxRate = dataPoints.maxOf { maxOf(it.rxRate, it.txRate, 1f) }
                val stepX = size.width / (dataPoints.size - 1).coerceAtLeast(1)

                for (i in 0 until dataPoints.size - 1) {
                    val p1 = dataPoints[i]
                    val p2 = dataPoints[i + 1]
                    val x1 = i * stepX
                    val x2 = (i + 1) * stepX
                    val y1Rx = size.height * (1 - p1.rxRate / maxRate)
                    val y2Rx = size.height * (1 - p2.rxRate / maxRate)
                    val y1Tx = size.height * (1 - p1.txRate / maxRate)
                    val y2Tx = size.height * (1 - p2.txRate / maxRate)

                    drawLine(rxColor, Offset(x1, y1Rx), Offset(x2, y2Rx), strokeWidth = 2f)
                    drawLine(txColor, Offset(x1, y1Tx), Offset(x2, y2Tx), strokeWidth = 2f)
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val latest = dataPoints.lastOrNull()
            if (latest != null) {
                Text("RX: ${formatRate(latest.rxRate)}", style = MaterialTheme.typography.labelSmall)
                Text("TX: ${formatRate(latest.txRate)}", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

private fun formatRate(bytesPerSec: Float): String {
    val kbps = bytesPerSec * 8 / 1000
    return when {
        kbps >= 1000 -> "%.1f Mbps".format(kbps / 1000)
        kbps >= 1 -> "%.0f Kbps".format(kbps)
        else -> "%.0f bps".format(bytesPerSec * 8)
    }
}
