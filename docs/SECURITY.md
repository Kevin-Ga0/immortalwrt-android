# Security Design

**Version**: 1.0
**Date**: 2026-05-21

## Network Security

### HTTPS Preference

- HTTPS is recommended and presented as the default
- Self-signed certificates use TOFU (Trust On First Use) strategy
- Certificate fingerprint changes block connection and alert the user
- No silent trust-all mode in Release builds

### HTTP Cleartext Policy

**Decision**: Allow HTTP with mandatory app-layer safeguards (方案A)

**Rationale**: Many ImmortalWrt/OpenWrt devices only expose HTTP. Android's Network Security Config is static and cannot dynamically whitelist arbitrary user-entered router IPs. Blocking HTTP at the platform level would exclude a significant portion of legitimate users.

**App-Layer Safeguards** (5 mandatory rules):

1. Only routers with user-confirmed HTTP risk acknowledgment may use `http://`
2. HTTPS failures MUST NOT auto-downgrade to HTTP
3. HTTP routers display a persistent "Insecure Connection" badge on list, detail, and diagnostics pages
4. Diagnostic reports must annotate `protocol=http`
5. No code path may bypass Router config to directly request arbitrary `http://` URLs

**Implementation**:
- `res/xml/network_security_config.xml` allows cleartext traffic
- Debug/Release variants via sourceSets (`src/debug/` and `src/release/`)
- App-layer check gates all HTTP requests through `RouterSecurityContext.httpRiskAccepted`

### TLS Configuration

- OkHttp enforces modern TLS (TLS 1.2+)
- Certificate pinning via TOFU fingerprint comparison
- Per-routerId trust anchors (changing fingerprint on router A does not affect router B)

## Credential Security

### Password Storage

- Passwords stored in Android Keystore via `SecretStore` abstraction
- Never stored in Room database (plaintext or otherwise)
- Credential deletion cascades on router removal
- "Clear all local data" option available in settings

### Session Management

- `ubus_rpc_session` tokens stored in memory only
- Never persisted to disk
- Logged sessions are redacted (only first 4 + last 4 hex chars)
- Session destroyed on logout or router removal

### Password Change Safety (Beta)

- Pending rotation pattern: old credentials preserved until new ones verified
- Sequence: change password → mark pending → clear session → re-login with new password → replace stored credential
- If re-login fails: prompt user that "password may have changed remotely, try logging in with new password"
- SecretStore never enters half-updated state
- Old and new passwords never logged

## Log Redaction

`SensitiveLogSanitizer` applies these rules:

| Content | Redaction |
|---|---|
| 32-char hex session ID | `abcd************************wxyz` (first 4 + last 4) |
| Password field | `******` |
| Authorization / Cookie header | `******` |
| MAC address | `AA:BB:**:**:**:**` |
| IPv4 address | `192.168.1.xxx` (optional, user-selectable) |
| Hostname | `<hidden>` (optional, user-selectable) |

Applied to: crash logs, diagnostic reports, Logcat output (Release builds).

## Operational Security

### MVP (Read-Only)
- No write operations to router
- No risk of configuration corruption

### Beta (Low-Risk Operations)
- **Reboot**: Requires confirmation dialog explaining impact; state machine with timeouts; no UCI writes
- **Change Password**: Requires current auth; pending rotation; no shell/exec/file bypass; graceful fallback to LuCI/SSH recommendation

### Forbidden Operations (All Versions)
- No UCI writes of any kind (MVP/Beta)
- No shell command execution
- No file system access on router
- No arbitrary ubus method calls outside approved list

## CI Security Checks

- `NoWifiGuardTest` blocks WiFi-related code from entering the codebase
- Lint rules for sensitive logging
- Secret detection in CI pipeline (prevent hardcoded keys/tokens)
- Release build verification: no debug logging, no test endpoints

## Threat Model Summary

| Threat | Severity | Mitigation |
|---|---|---|
| Credential interception (HTTP) | High | HTTP risk acknowledgment + persistent insecure badge |
| Self-signed cert MITM | High | TOFU fingerprint pinning + change detection |
| Credential leak via logs | High | Log redaction + SecretStore + no Room storage |
| Session hijacking | Medium | Session in-memory only + redacted logging |
| Accidental WiFi config change | Medium | WiFi completely excluded from codebase + CI enforcement |
| Password change race condition | Medium | Pending rotation pattern + verified re-login |
| Unauthorized router access | Low | Per-routerId isolation + session expiry |
