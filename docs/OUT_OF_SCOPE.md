# Out of Scope

本项目不包含任何 WiFi 相关能力。This project contains zero WiFi functionality.

## 禁止功能 | Forbidden Features

- WiFi 状态展示 | WiFi status display
- SSID 管理 | SSID management
- WiFi 密码修改 | WiFi password modification
- 无线客户端展示 | Wireless client display
- 无线扫描 | Wireless scanning
- 无线开关 | Wireless radio toggle
- 无线重载 | Wireless reload
- 信道/带宽/加密配置 | Channel/bandwidth/encryption config

## 禁止 API | Forbidden APIs

- `network.wireless.*` (status, dump, any method)
- `iwinfo.*` (assoclist, info, any method)
- `hostapd.*` (any method)
- `uci get wireless`
- `uci set wireless`
- `/etc/config/wireless` read/write

## 禁止文件 | Forbidden Files

- `WirelessScreen.kt`
- `WirelessRepository.kt` / `WirelessRepositoryImpl.kt`
- `WirelessRemoteDataSource.kt`
- `WirelessInfo.kt` / `WirelessDevice.kt`
- `WifiEditDialog.kt`
- `Wifi*.kt` / `Iwinfo*.kt` / `Hostapd*.kt`

## 禁止 UCI 写入 (MVP/Beta) | Forbidden UCI Writes (MVP/Beta)

- `uci.set` of any kind
- `uci.commit` of any kind
- Network configuration changes
- DHCP configuration changes
- Firewall configuration changes

## CI Enforcement

Phase 1+ CI includes `NoWifiGuardTest` that scans source trees for forbidden keywords:

**Tier 1 (hard block)**:
```
network.wireless | iwinfo | hostapd | uci.get wireless | uci.set wireless |
WirelessRepository | WirelessRemoteDataSource | WirelessScreen | WifiEditDialog
```

**Tier 2 (warning, .kt/.kts/.xml/.json only)**:
```
wifi | WiFi | wireless | SSID | BSSID | radio0 | radio1 | /etc/config/wireless
```

## Rationale

This app is designed to avoid WiFi misconfiguration risk and wireless client privacy concerns. Users who need WiFi management should use the router's LuCI web interface or SSH.

WiFi was removed from scope to:
1. Eliminate risk of accidental wireless misconfiguration from a mobile device
2. Avoid privacy implications of surfacing wireless client data on a phone
3. Simplify the security model (no wireless config = fewer attack surfaces)
4. Keep the app focused on system/network monitoring
