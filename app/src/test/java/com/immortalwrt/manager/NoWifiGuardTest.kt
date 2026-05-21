package com.immortalwrt.manager

import org.junit.Test
import java.io.File

class NoWifiGuardTest {
    private val sourceRoot = File("app/src/main/java/com/immortalwrt/manager")

    private val tier1HardBlock = listOf(
        "network.wireless", "iwinfo", "hostapd",
        "uci.get wireless", "uci.set wireless",
        "WirelessRepository", "WirelessRemoteDataSource",
        "WirelessScreen", "WifiEditDialog"
    )

    private val tier2Warning = listOf(
        "wifi", "WiFi", "wireless", "SSID", "BSSID",
        "radio0", "radio1", "/etc/config/wireless"
    )

    @Test
    fun noWifiFilesExist() {
        val forbiddenFiles = listOf(
            "WirelessScreen.kt", "WirelessRepository.kt", "WirelessRemoteDataSource.kt",
            "WirelessInfo.kt", "WirelessDevice.kt", "WifiEditDialog.kt",
            "WifiConfig.kt", "WifiScan.kt"
        )
        val allFiles = sourceRoot.walkTopDown().filter { it.isFile }.map { it.name }.toList()
        for (forbidden in forbiddenFiles) {
            assert(!allFiles.any { it.equals(forbidden, ignoreCase = true) }) {
                "FORBIDDEN FILE FOUND: $forbidden. WiFi-related code is explicitly out of scope. See docs/OUT_OF_SCOPE.md"
            }
        }
    }

    @Test
    fun noWifiCodeInSourceFiles() {
        val kotlinFiles = sourceRoot.walkTopDown()
            .filter { it.isFile && (it.extension == "kt" || it.extension == "kts") }
            .toList()

        for (file in kotlinFiles) {
            val content = file.readText()

            // Skip this test file itself
            if (file.name == "NoWifiGuardTest.kt") continue

            for (keyword in tier1HardBlock) {
                val found = content.contains(keyword, ignoreCase = false)
                assert(!found) {
                    "TIER 1 VIOLATION in ${file.relativeTo(sourceRoot)}: Found forbidden keyword '$keyword'. " +
                    "WiFi-related code is explicitly out of scope. See docs/OUT_OF_SCOPE.md"
                }
            }
        }
    }

    @Test
    fun noWifiImportsOrDependencies() {
        // Check that no forbidden packages are imported
        val forbiddenImports = listOf(
            "network.wireless", "iwinfo", "hostapd"
        )
        val kotlinFiles = sourceRoot.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .toList()

        for (file in kotlinFiles) {
            if (file.name == "NoWifiGuardTest.kt") continue
            val content = file.readText()
            for (keyword in forbiddenImports) {
                assert(!content.contains(keyword)) {
                    "FORBIDDEN IMPORT in ${file.name}: $keyword"
                }
            }
        }
    }

    @Test
    fun noUbiWifiMethodsExposed() {
        // Ensure no ubus Wi-Fi methods are called
        val forbiddenUbusCalls = listOf(
            "\"network.wireless\"",
            "\"iwinfo\"",
            "\"hostapd\""
        )
        val kotlinFiles = sourceRoot.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .toList()

        for (file in kotlinFiles) {
            if (file.name == "NoWifiGuardTest.kt") continue
            val content = file.readText()
            for (keyword in forbiddenUbusCalls) {
                assert(!content.contains(keyword)) {
                    "FORBIDDEN UBUS CALL in ${file.name}: $keyword"
                }
            }
        }
    }
}
