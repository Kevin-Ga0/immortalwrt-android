# PRD: ImmortalWrt Android Manager (No WiFi Edition)

**Version**: 1.0
**Date**: 2026-05-21
**Status**: Draft

## Problem Statement

ImmortalWrt / OpenWrt users currently need a desktop browser to check router status via LuCI. There is no native Android app for quick, read-only status checks. Existing solutions either bundle WiFi management (adding complexity and risk) or require SSH (not mobile-friendly).

## Product Vision

A native Android app that lets users securely connect to one or more ImmortalWrt / OpenWrt routers, view system and network status, see connected devices, and perform a small set of low-risk operations — all without touching WiFi configuration.

## Target Users

- Home lab / homelab operators running ImmortalWrt on x86_64 or ARM devices
- Network enthusiasts who want quick status checks from their phone
- Users who prefer read-only visibility over configuration changes

## Core Principles

1. **Read-only first** — MVP does zero writes to router configuration
2. **No WiFi** — This edition explicitly excludes all wireless management
3. **Graceful degradation** — Missing optional APIs never block core functionality
4. **Security by default** — HTTPS preferred, TOFU for self-signed certs, secrets in Keystore
5. **Multi-router** — Manage multiple routers with isolated sessions and credentials

## MVP Scope

- Add/edit/delete routers (HTTP/HTTPS)
- Endpoint auto-discovery (3-layer probe: connectivity → endpoint → auth)
- Dashboard (model, firmware, uptime, load, memory, WAN IP, device count)
- Network interfaces (WAN/LAN/Bridge/VLAN, IP/MAC, RX/TX)
- Connected devices (DHCP leases + host hints, deduplicated by MAC)
- System info (firmware, kernel, local time, environment)
- Diagnostics page with redacted report export
- Offline cache of last-known state
- Unified UI state model (Loading/Content/Empty/Error/Cached/Unsupported)

## Beta Scope

- Router reboot (with full state machine and timeout handling)
- Change current user password (with pending rotation safety)
- Real-time traffic chart

## GA Scope

- i18n (Simplified Chinese + English)
- Dark mode
- TalkBack accessibility
- Release signing
- CI/CD pipeline
- Privacy policy + permissions disclosure
- User guide + FAQ
- THIRD_PARTY_LICENSES.md

## Explicitly Out of Scope

See [OUT_OF_SCOPE.md](./OUT_OF_SCOPE.md) for the complete WiFi exclusion list.

## Technical Approach

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM + Repository + UseCase
- **Async**: Kotlin Coroutines + Flow
- **Network**: OkHttp + Moshi
- **DI**: Hilt
- **Storage**: Room + DataStore + Android Keystore
- **Min SDK**: 26 (Android 8.0)

## API Communication

The app communicates with routers via ubus JSON-RPC over HTTP/HTTPS:
- `session.login` — authentication
- `system.board` / `system.info` — system information
- `network.interface.dump` / `network.interface.<name>.status` — network state
- `luci-rpc.getHostHints` / `luci-rpc.getDHCPLeases` — device discovery
- `luci.getRealtimeStats` — traffic monitoring
- `system.reboot` — reboot (Beta)
- `luci.setPassword` — password change (Beta)

See [API_COMPATIBILITY.md](./API_COMPATIBILITY.md) for the full capability matrix.

## Security

See [SECURITY.md](./SECURITY.md) for detailed security decisions including HTTP cleartext policy, TOFU certificate strategy, and credential storage.

## Privacy

See [PRIVACY.md](./PRIVACY.md) for the privacy policy. All router data stays local by default.
