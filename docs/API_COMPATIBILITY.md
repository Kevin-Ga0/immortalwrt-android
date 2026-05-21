# API Compatibility

**Version**: 2.0
**Date**: 2026-05-21
**Status**: Verified against ImmortalWrt (OpenWrt 23.05) source code

## Source Code Analysis Basis

The following information is derived from direct analysis of the ImmortalWrt source tree at `/home/kevin/immortalwrt`:

| Component | Source Location | Key Files |
|---|---|---|
| rpcd | `package/system/rpcd/` | Makefile (plugins: file, rpcsys, iwinfo, ucode) |
| rpcd-mod-luci | `feeds/luci/libs/rpcd-mod-luci/src/luci.c` | ubus object `luci-rpc` (6 methods) |
| rpcd-mod-rrdns | `feeds/luci/libs/rpcd-mod-rrdns/src/rrdns.c` | ubus object `network.rrdns` (1 method) |
| luci-base (ucode) | `feeds/luci/modules/luci-base/root/usr/share/rpcd/ucode/luci` | ubus object `luci` (10+ methods) |
| luci-mod-status | `feeds/luci/modules/luci-mod-status/` | `luci-bwc` C daemon for realtime stats |
| luci-mod-rpc | `feeds/luci/modules/luci-mod-rpc/luasrc/` | cgi-bin JSON-RPC proxy (uci/fs/sys/ipkg/ip/auth) |
| uhttpd | `package/network/services/uhttpd/` | ubus.default sets prefix=/ubus |

## Required Router Packages

For each capability tier, the following OpenWrt packages must be installed:

### MVP Minimum (Read-Only Dashboard)

| Package | Provides | Required For |
|---|---|---|
| `rpcd` | ubus RPC backend daemon | All API access |
| `rpcd-mod-file` | file operations plugin | `system.board` (via /etc/board.json read) |
| `rpcd-mod-rpcsys` | sysupgrade/password plugin | `system.info` |
| `rpcd-mod-ucode` | ucode script plugin | `luci.*` methods (getVersion, getTempInfo, etc.) |
| `rpcd-mod-luci` | LuCI core C plugin | `luci-rpc.*` methods (getHostHints, getDHCPLeases, getNetworkDevices) |
| `uhttpd` | HTTP server | HTTP transport for `/ubus` endpoint |
| `uhttpd-mod-ubus` | ubus HTTP module | Exposes ubus over HTTP at `/ubus` |
| `luci-base` | LuCI base (includes ucode plugin) | `luci` ubus object |
| `luci-mod-status` | Status module (includes luci-bwc) | `luci.getRealtimeStats` |

### Optional Enhancement

| Package | Provides | Required For |
|---|---|---|
| `rpcd-mod-rrdns` | Reverse DNS lookup | `network.rrdns.lookup` (used by `luci-rpc.getHostHints`) |
| `rpcd-mod-iwinfo` + `libiwinfo` | Wireless info | `luci-rpc.getWirelessDevices` (NOT used by this app) |
| `rpcd-mod-rad2-enc` | Radicale password hashing | Not relevant to this app |
| `luci-mod-rpc` | cgi-bin JSON-RPC proxy | `/cgi-bin/luci/admin/ubus` endpoint (alternative endpoint) |

## ubus JSON-RPC Protocol

### Direct `/ubus` Endpoint (uhttpd-mod-ubus)

The app communicates directly with ubus through uhttpd-mod-ubus. The method name in JSON-RPC is the **ubus object name**, and `params[0]` is the **ubus method name**:

```json
{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "<ubus_object>",
    "params": ["<ubus_method>", { ... }]
}
```

**Example — login:**
```json
{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "session",
    "params": ["login", {"username": "root", "password": "xxx"}]
}
```

**Example — system board:**
```json
{
    "jsonrpc": "2.0",
    "id": 2,
    "method": "system",
    "params": ["board", {}]
}
```

**Session authentication**: After `session.login`, the returned `ubus_rpc_session` token is passed as `params[0]` before the ubus method in subsequent calls:

```json
{
    "jsonrpc": "2.0",
    "id": 3,
    "method": "session",
    "params": ["<ubus_rpc_session>", "<ubus_object>", "<ubus_method>", { ... }]
}
```

**Example — authenticated call to luci-rpc.getHostHints:**
```json
{
    "jsonrpc": "2.0",
    "id": 5,
    "method": "session",
    "params": ["abc123...def456", "luci-rpc", "getHostHints", {}]
}
```

Login response returns `ubus_rpc_session` (32-char hex string):
```json
{
    "jsonrpc": "2.0",
    "id": 1,
    "result": [0, {"ubus_rpc_session": "abc123...def456", ...}]
}
```

### LuCI Proxy `/cgi-bin/luci/admin/ubus` Endpoint

Alternative endpoint that proxies ubus through LuCI's auth layer (luci-mod-rpc). The JSON-RPC method is always `"call"` and params are `[sid, object, method, args]`:

```json
{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "call",
    "params": ["<luci_sid>", "<ubus_object>", "<ubus_method>", { ... }]
}
```

URL path suffix can batch multiple calls: `/cgi-bin/luci/admin/ubus/<object1>.<method1>;<object2>.<method2>`

**Source reference**: `rpc.js:341-343` — `params: [rpcSessionID, options.object, options.method, params]`
**Source reference**: `rpc.js:7` — `rpcBaseURL = L.url('admin/ubus')` (maps to `/cgi-bin/luci/admin/ubus`)

### ubus Error Codes

| Code | Meaning |
|---|---|
| 0 | Command OK |
| 1 | Invalid command |
| 2 | Invalid argument |
| 3 | Method not found |
| 4 | Resource not found |
| 5 | No data received |
| 6 | Permission denied |
| 7 | Request timeout |
| 8 | Not supported |
| 9 | Unspecified error |
| 10 | Connection lost |

## Endpoint Discovery Sequence

1. User-specified endpoint (if provided)
2. `/ubus` (direct ubus — uhttpd-mod-ubus, default prefix)
3. `/cgi-bin/luci/admin/ubus` (LuCI JSON-RPC proxy)
4. `/admin/ubus` (alternate LuCI proxy path)

Each endpoint is probed with a `session.login` request. A valid JSON-RPC response (with `result` or `error` field) confirms endpoint availability. HTML login pages or 404 responses are NOT valid endpoints.

**Source reference**: `rpc.js:7` — `rpcBaseURL = L.url('admin/ubus')` maps to `/cgi-bin/luci/admin/ubus`
**Source reference**: `ubus.default` — sets `uhttpd.main.ubus_prefix=/ubus`

---

## Capability Matrix (Verified)

### Tier 1: Core Authentication & System (MVP Required)

| Capability | ubus Object | ubus Method | Params | Required | Source |
|---|---|---|---|---|---|
| Login | `session` | `login` | `username`, `password`, `timeout` (optional) | **Yes** | ubusd built-in |
| System board | `system` | `board` | none | **Yes** | rpcd (reads /etc/board.json) |
| System info | `system` | `info` | none | **Yes** | rpcd-mod-rpcsys |

### Tier 2: Network Interfaces (MVP Required)

| Capability | ubus Object | ubus Method | Params | Required | Source |
|---|---|---|---|---|---|
| Interface dump | `network.interface` | `dump` | none | **Yes** | netifd |
| Interface status | `network.interface` | `status` | `interface` (string: ifname) | **Yes** | netifd |
| Device status | `network.device` | `status` | `name` (string: "br-lan", etc.) | No | netifd |

### Tier 3: Enhanced Device & Network Info (MVP Optional)

| Capability | ubus Object | ubus Method | Params | Required | Source |
|---|---|---|---|---|---|
| Host hints | `luci-rpc` | `getHostHints` | none | No | rpcd-mod-luci (`luci.c:1794`) |
| DHCP leases (IPv4) | `luci-rpc` | `getDHCPLeases` | `family` (int32: 4) | No | rpcd-mod-luci (`luci.c:1904`) |
| DHCP leases (IPv6) | `luci-rpc` | `getDHCPLeases` | `family` (int32: 6) | Optional | rpcd-mod-luci |
| DHCP leases (All) | `luci-rpc` | `getDHCPLeases` | `family` (int32: 0 or omit) | No | rpcd-mod-luci |
| Network devices | `luci-rpc` | `getNetworkDevices` | none | No | rpcd-mod-luci (`luci.c:842`) |
| Board JSON | `luci-rpc` | `getBoardJSON` | none | No | rpcd-mod-luci (`luci.c:1881`) |
| DUID hints | `luci-rpc` | `getDUIDHints` | none | No | rpcd-mod-luci (`luci.c:1813`) |

### Tier 4: Realtime & System Status (MVP Optional)

| Capability | ubus Object | ubus Method | Params | Required | Source |
|---|---|---|---|---|---|
| Realtime bandwidth | `luci` | `getRealtimeStats` | `mode`="interface", `device`="eth0" | No | ucode plugin (`luci:498`) → luci-bwc |
| Realtime connections | `luci` | `getRealtimeStats` | `mode`="conntrack" | No | ucode plugin → luci-bwc |
| Realtime load | `luci` | `getRealtimeStats` | `mode`="load" | No | ucode plugin → luci-bwc |
| Temperature | `luci` | `getTempInfo` | none | No | ucode plugin (`10_system.js:36`) |
| CPU info | `luci` | `getCPUInfo` | none | No | ucode plugin (`10_system.js:26`) |
| CPU usage | `luci` | `getCPUUsage` | none | No | ucode plugin (`10_system.js:31`) |
| CPU benchmark | `luci` | `getCPUBench` | none | No | ucode plugin (`10_system.js:21`) |
| LuCI version | `luci` | `getVersion` | none | No | ucode plugin (`10_system.js:6`) |
| Conntrack list | `luci` | `getConntrackList` | none | No | ucode plugin (`luci:534`) |
| Process list | `luci` | `getProcessList` | none | No | ucode plugin (`luci:540`) |
| Ethernet ports | `luci` | `getBuiltinEthernetPorts` | none | No | ucode plugin (`luci:546`) |

### Tier 5: Beta Operations

| Capability | ubus Object | ubus Method | Params | Required | Source |
|---|---|---|---|---|---|
| Reboot | `system` | `reboot` | none | Beta | rpcd-mod-rpcsys |
| Change password | `luci` | `setPassword` | `{username, password}` | Beta | ucode plugin (`luci:397-406`) |
| UCI get | `uci` | `get` | `config`, `section`, `option` | No | rpcd uci plugin |

### Tier 6: Wireless (Explicitly Excluded)

| Capability | ubus Object | ubus Method | Status | Source |
|---|---|---|---|---|
| Wireless status | `network.wireless` | `status` | **FORBIDDEN** | netifd |
| Wireless devices | `luci-rpc` | `getWirelessDevices` | **FORBIDDEN** | rpcd-mod-luci (`luci.c:1156`) |
| Wireless realtime | `luci` | `getRealtimeStats` | `mode`="wireless" forbidden | ucode plugin |
| iwinfo info | `iwinfo` | `info` | **FORBIDDEN** | rpcd-mod-iwinfo |

## `luci-rpc.getHostHints` Return Details

**Source**: `luci.c:1793-1809`

This method aggregates data from 5 sources with priority ordering:

| Source | Priority | Data | Implementation |
|---|---|---|---|
| Neighbor table (netlink) | 10 | MAC→IPv4/IPv6 via RTM_GETNEIGH | `luci.c:1316-1354` |
| /etc/ethers | 50 | MAC→IPv4/hostname | `luci.c:1408-1442` |
| DHCP lease files | 100 | MAC→IP/hostname (dnsmasq + odhcpd) | `luci.c:392-626` |
| Interface addresses | 200 | MAC→IP (getifaddrs) | `luci.c:1566-1640` |
| Static DHCP leases | 250 | UCI dhcp host sections | `luci.c:1444-1535` |
| Reverse DNS | — | IP→hostname (via network.rrdns) | `luci.c:1691-1739` |

Return format: `{ "<MAC>": { "ipaddrs": [...], "ip6addrs": [...], "name": "<hostname>" } }`

## `luci-rpc.getDHCPLeases` Return Details

**Source**: `luci.c:1903-1996`

- Reads from `/tmp/dhcp.leases` (dnsmasq) and `/tmp/hosts/odhcpd` (odhcpd)
- Supports `family` parameter: 0=all, 4=IPv4 only, 6=IPv6 only
- Each lease includes: `expires`, `hostname`, `macaddr`, `duid`, `ipaddr`/`ip6addr`, `ip6addrs` (array with prefix lengths)

## `luci-rpc.getNetworkDevices` Return Details

**Source**: `luci.c:629-878`

For each network interface in `/sys/class/net/`, returns:

| Field | Source | Type |
|---|---|---|
| name | if_name | string |
| bridge | /sys/class/net/{name}/brif exists | bool |
| ports | /sys/class/net/{name}/brif/* | string[] |
| id | /sys/class/net/{name}/bridge/bridge_id | string |
| stp | /sys/class/net/{name}/bridge/stp_state | bool |
| master | /sys/class/net/{name}/master (readlink) | string |
| wireless | /sys/class/net/{name}/phy80211/index | bool |
| up | /sys/class/net/{name}/operstate | bool |
| mtu | /sys/class/net/{name}/mtu | u32 |
| qlen | /sys/class/net/{name}/tx_queue_len | u32 |
| devtype | /sys/class/net/{name}/uevent DEVTYPE | string |
| mac | getifaddrs() AF_PACKET | string |
| type | sll_hatype | u32 |
| ifindex | sll_ifindex | u32 |
| parent | iflink mismatch resolution | string |
| ipaddrs[] | getifaddrs() AF_INET | {address, netmask, broadcast/remote} |
| ip6addrs[] | getifaddrs() AF_INET6 | {address, netmask, broadcast/remote} |
| stats{} | /sys/class/net/{name}/statistics/* | {rx_bytes, tx_bytes, tx_errors, rx_errors, tx_packets, rx_packets, multicast, collisions, rx_dropped, tx_dropped} |
| flags{} | ifa_flags | {up, broadcast, promisc, loopback, noarp, multicast, pointtopoint} |
| link{} | /sys/class/net/{name}/* | {speed, duplex, carrier, changes, up_count, down_count} |

## `luci.getRealtimeStats` Return Details

**Source**: `luci-bwc.c` + ucode plugin (`luci:498-532`)

Executes `luci-bwc` binary with mode-specific flags. Returns 60 data points (STEP_COUNT=60, STEP_TIME=1s).

**`mode="interface"`** → `luci-bwc -i <device>`:
```
[[time, rx_bytes, rx_packets, tx_bytes, tx_packets], ...]
```

**`mode="conntrack"`** → `luci-bwc -c`:
```
[[time, udp_count, tcp_count, other_count], ...]
```

**`mode="load"`** → `luci-bwc -l`:
```
[[time, load1*100, load5*100, load15*100], ...]
```

**Important**: `luci-bwc` must be running as a daemon to collect data. It is started on first invocation (pid file at `/var/run/luci-bwc.pid`) and keeps running for TIMEOUT (default 10 seconds) after last query. The JS web UI polls every 3 seconds with SIGUSR1 to reset the countdown.

## `system.board` Return Details

Reads `/etc/board.json`. Typical return fields:
- `hostname` — Router hostname
- `model` — Hardware model string
- `system` — Architecture (e.g., "ARMv8 Processor rev 4", "x86/64")
- `kernel` — Kernel version string
- `release.target` — Target platform (e.g., "x86/64", "ramips/mt7621")
- `release.description` — Firmware description (e.g., "ImmortalWrt 23.05")

## `system.info` Return Details

Typical return fields:
- `uptime` — System uptime in seconds
- `localtime` — Local time as Unix timestamp
- `load` — Array of 3 uint16s: [load1, load5, load15] (scaled by 65535)
- `memory.total` — Total RAM
- `memory.free` — Free RAM
- `memory.buffered` — Buffered RAM
- `swap.total` — Total swap
- `swap.free` — Free swap

## MVP Minimum Capability Threshold

The following 3 calls MUST all pass for MVP core read-only functionality:

1. `session.login` — Authentication
2. `system.board` — Device identification
3. `network.interface.dump` — Interface list

If any of these fail, MVP core features will be degraded. The app shows which capability is missing and suggests checking router configuration.

**Required packages for minimum threshold**:
```
rpcd + rpcd-mod-file + rpcd-mod-rpcsys + uhttpd + uhttpd-mod-ubus
```

**For device list with MAC addresses (recommended)**:
```
rpcd-mod-luci (+ rpcd-mod-rrdns for hostname resolution)
```

**For realtime graphs**:
```
luci-base + luci-mod-status
```

---

## Router Environment

- **Model**: [fill]
- **Firmware**: [fill] (ImmortalWrt / OpenWrt version)
- **Kernel**: [fill]
- **LuCI**: [fill] (version or N/A)
- **rpcd**: [fill] (version or N/A)
- **uhttpd-mod-ubus**: [fill] (installed / not installed)
- **luci-base**: [fill] (installed / not installed — needed for `luci` ubus object)
- **rpcd-mod-luci**: [fill] (installed / not installed — needed for `luci-rpc` ubus object)
- **rpcd-mod-rrdns**: [fill] (installed / not installed — needed for reverse DNS)
- **rpcd-mod-ucode**: [fill] (installed / not installed — needed for `luci` ubus object)
- **luci-mod-status**: [fill] (installed / not installed — needed for `luci-bwc` realtime stats)
- **Endpoint**: [fill] (/ubus or /cgi-bin/luci/admin/ubus or /admin/ubus)
- **Scheme**: [fill] (HTTP / HTTPS)

---

## Test Record

### Device #1

| Field | Value |
|---|---|
| **Date tested** | |
| **Model** | |
| **Architecture** | |
| **Firmware** | |
| **Kernel** | |
| **LuCI** | |
| **rpcd** | |
| **uhttpd-mod-ubus** | |
| **luci-base** | |
| **rpcd-mod-luci** | |
| **rpcd-mod-rrdns** | |
| **rpcd-mod-ucode** | |
| **luci-mod-status** | |
| **Endpoint used** | |
| **Scheme** | |

| Capability | Result | Notes |
|---|---|---|
| session.login | | |
| system.board | | |
| system.info | | |
| network.interface.dump | | |
| network.interface.{name}.status | | |
| network.device.status | | |
| luci-rpc.getHostHints | | |
| luci-rpc.getDHCPLeases (family=4) | | |
| luci-rpc.getDHCPLeases (family=6) | | |
| luci-rpc.getNetworkDevices | | |
| luci-rpc.getBoardJSON | | |
| luci-rpc.getDUIDHints | | |
| luci.getVersion | | |
| luci.getTempInfo | | |
| luci.getCPUInfo | | |
| luci.getCPUUsage | | |
| luci.getCPUBench | | |
| luci.getRealtimeStats (interface) | | |
| luci.getRealtimeStats (load) | | |
| luci.getRealtimeStats (conntrack) | | |
| system.reboot | | |
| luci-rpc.setPassword | | |

### Device #2

*(Duplicate the table above for each additional device)*

---

## Known Compatibility Notes

### ubus ACL Requirements

rpcd performs ACL checks. The `unauthenticated.json` ACL file at `/usr/share/rpcd/acl.d/unauthenticated.json` controls which ubus methods are available without login. Method access may fail with ubus error code 6 (Permission denied) if the ACL doesn't grant access.

The LuCI packages install ACL files at `/usr/share/rpcd/acl.d/` to grant necessary permissions:
- `luci-base` installs ACL for `luci` object methods
- `luci-mod-status` installs ACL for `luci.getRealtimeStats`, `luci.getCPUBench`, etc.

### Session Management

- `ubus_rpc_session` tokens are 32-character hex strings
- Sessions have configurable timeout (default from LuCI config)
- Session tokens are never persisted to disk by rpcd

### luci-bwc Daemon Lifecycle

- First `luci.getRealtimeStats` call starts `luci-bwc` daemon
- Daemon runs for TIMEOUT seconds (default 10) after last query
- Web UI resets countdown via SIGUSR1 every 3 seconds
- App must poll at intervals < 10 seconds to keep daemon alive
- Data files stored at `/var/lib/luci-bwc/` (if/, radio/, connections, load)
