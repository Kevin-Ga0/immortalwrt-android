package com.immortalwrt.manager.core.util

object FormatUtils {
    fun formatBytes(bytes: Long): String {
        val kb = bytes / 1024
        val mb = kb / 1024
        val gb = mb / 1024
        return when {
            gb > 0 -> "%.1f GB".format(gb.toDouble())
            mb > 0 -> "%.0f MB".format(mb.toDouble())
            kb > 0 -> "%.0f KB".format(kb.toDouble())
            else -> "${bytes} B"
        }
    }

    fun formatUptime(seconds: Long): String {
        val days = seconds / 86400
        val hours = (seconds % 86400) / 3600
        val mins = (seconds % 3600) / 60
        return if (days > 0) "${days}d ${hours}h ${mins}m" else "${hours}h ${mins}m"
    }

    fun formatRate(bytesPerSec: Float): String {
        val kbps = bytesPerSec * 8 / 1000
        return when {
            kbps >= 1000 -> "%.1f Mbps".format(kbps / 1000)
            kbps >= 1 -> "%.0f Kbps".format(kbps)
            else -> "%.0f bps".format(bytesPerSec * 8)
        }
    }
}
