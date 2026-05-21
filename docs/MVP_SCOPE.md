# MVP Scope

**Version**: 1.0
**Date**: 2026-05-21

## MVP Definition

MVP is a **read-only** Android app. It performs zero writes to router configuration (no UCI set, no network config changes). It only reads system state and displays it.

## Feature Checklist

### Router Management
- [ ] Add router (name, host, port, scheme, username, password, endpoint)
- [ ] Edit router
- [ ] Delete router (cascades: session, cache, credentials, cert trust)
- [ ] Router list with connection state indicator
- [ ] Test connection (3-layer probe: connectivity → endpoint → auth)

### Authentication & Session
- [ ] ubus JSON-RPC session.login
- [ ] Session per routerId isolation
- [ ] Auto-refresh expiring session (once)
- [ ] Session expiry handling with clear error messaging
- [ ] Session destroy on router removal

### Certificate Trust (TOFU)
- [ ] HTTPS self-signed cert fingerprint display
- [ ] User confirmation dialog for first connection
- [ ] Fingerprint storage per router
- [ ] Fingerprint change detection and blocking

### HTTP Risk Acknowledgment
- [ ] HTTP risk confirmation dialog before saving
- [ ] Persistent insecure badge on HTTP routers (list, detail, diagnostics)

### Dashboard
- [ ] Router model / architecture
- [ ] Firmware version / kernel version
- [ ] Uptime
- [ ] System load
- [ ] Memory usage (total/free/available)
- [ ] WAN IP address
- [ ] Online device count
- [ ] Pull-to-refresh
- [ ] Cached data indicator

### Network Interfaces
- [ ] Interface list (WAN, LAN, Bridge, VLAN)
- [ ] Per-interface: IP address, MAC, RX bytes, TX bytes
- [ ] Protocol type (static/dhcp/pppoe)
- [ ] Interface up/down status badge
- [ ] DNS servers (if available)
- [ ] Gateway (if available)
- [ ] Fallback: basic fields when device.status unavailable

### Connected Devices
- [ ] DHCP IPv4 lease list
- [ ] Host hints aggregation
- [ ] MAC-based deduplication (case/separator normalized)
- [ ] Multi-source labels (DHCP, Host Hint, Neighbor)
- [ ] Search/filter
- [ ] Online/offline indication
- [ ] Last seen timestamp
- [ ] Graceful fallback when APIs unavailable

### System Info
- [ ] Firmware version / build info
- [ ] Kernel version
- [ ] Local time / uptime
- [ ] Available ubus capabilities
- [ ] Router environment (distribution, version, LuCI/rpcd status)
- [ ] Endpoint mode (direct ubus / LuCI proxy / custom)

### Diagnostics
- [ ] 3-layer probe status display
- [ ] Protocol type + HTTP insecure indicator
- [ ] Recent error log
- [ ] Redacted Markdown report preview
- [ ] Export with anonymization options (IP, MAC, hostname)
- [ ] Copy to clipboard

### UI Patterns
- [ ] Unified LoadableUiState on all pages
- [ ] Material 3 theme
- [ ] Bottom navigation (Dashboard, Network, Devices, System)
- [ ] Snackbar for transient messages
- [ ] Pull-to-refresh where applicable

## What MVP Does NOT Include

- WiFi status / SSID / wireless client display
- LAN / WAN / DHCP / firewall configuration changes
- UCI write operations of any kind
- Router reboot
- Password change
- Real-time traffic monitoring
- Background monitoring / notifications
- Auto-discovery of routers
- i18n (English only)
- Dark mode
