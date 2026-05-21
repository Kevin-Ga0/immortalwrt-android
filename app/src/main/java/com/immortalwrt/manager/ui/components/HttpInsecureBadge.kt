package com.immortalwrt.manager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.immortalwrt.manager.ui.theme.InsecureBadge

@Composable
fun HttpInsecureBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(InsecureBadge)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text("HTTP", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onError)
    }
}
