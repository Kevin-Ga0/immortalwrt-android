# Privacy Policy & Data Handling

**Version**: 1.0
**Date**: 2026-05-21

## Core Privacy Principle

**All router information is stored locally on the device. Nothing is uploaded to any server by default.**

## Data Storage

### What is stored locally

| Data | Storage | Encrypted | Notes |
|---|---|---|---|
| Router name, host, port, scheme | Room | No | Basic connection metadata |
| Router environment info | Room | No | Firmware version, LuCI/rpcd status |
| Router capability results | Room | No | Which APIs are available |
| System status snapshots | Room | No | Cached dashboard/network/device data |
| User preferences | DataStore | No | Theme, language |
| Router password | Android Keystore | Yes | Via SecretStore abstraction |
| Certificate fingerprints | Room/DataStore | No | TOFU trust anchors |
| Diagnostic events | Room | No | Recent errors for troubleshooting |

### What is NOT stored

- `ubus_rpc_session` tokens (memory only)
- Plaintext passwords outside Keystore
- Complete session IDs in logs

### Data Deletion

- Deleting a router removes: connection info, session, cache, credentials, cert trust
- "Clear all data" option removes all routers, credentials, and cached data
- App uninstall removes all app-local data (standard Android behavior)

## Network Communications

### What the app sends to routers

- ubus JSON-RPC requests: `session.login`, `system.board`, `system.info`, `network.interface.dump`, `network.interface.<name>.status`, `luci-rpc.*`, `luci.*`
- These are direct connections from the phone to the user's router(s)
- No intermediary servers are involved

### What the app sends to third parties

**Nothing by default.** The app does not:
- Upload router addresses, device lists, MAC addresses, or hostnames
- Send diagnostic data to any external service
- Include analytics, crash reporting, or telemetry SDKs
- Contact any server other than the user-configured routers

### Future Telemetry (if ever introduced)

If crash reporting or telemetry is introduced in a future version:
- It will be **opt-in** (disabled by default)
- Users will be explicitly informed about what data is collected
- Router addresses, MAC addresses, hostnames, and LAN IPs will never be included

## Sensitive Local Network Information

The following data types are classified as **sensitive local network information**:
- Device lists (MAC addresses, hostnames, LAN IP addresses)
- Router connection details (host, port, credentials)
- Network topology information (interfaces, subnets)

These are stored exclusively on the user's device and are never transmitted off-device except:
- When the user explicitly exports a diagnostic report
- Diagnostic exports offer anonymization options before saving/sharing

## Diagnostic Reports

When a user exports a diagnostic report:
- Preview is shown before export
- User can choose to anonymize: IP addresses, MAC addresses, hostnames
- Report is saved as a local file; sharing is user-initiated
- Report filename: `immortalwrt-manager-diagnostic-YYYYMMDD-HHMMSS.md`

## Permissions

The app requests only:
- `INTERNET` — to communicate with user-configured routers

No other Android permissions are required for MVP.

## Data Retention

- All data is stored locally for the lifetime of the app installation
- Cached status snapshots are overwritten on each refresh
- Diagnostic events are retained up to a configurable maximum (default: 100 events)
- No server-side data retention (there is no server)

## Compliance

- No user accounts, no authentication backend, no cloud storage
- GDPR: All data is user-controlled and locally stored; deletion is via router removal or app uninstall
- CCPA: No data sale or sharing with third parties

## Changes to This Policy

Future versions that change data handling will:
- Notify users on first launch after update
- Require explicit acknowledgment for any new data collection
- Never silently introduce telemetry or third-party data sharing
