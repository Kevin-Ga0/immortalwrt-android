# ImmortalWrt Manager

Android 管理应用，用于通过 ubus JSON-RPC 协议远程管理 ImmortalWrt / OpenWrt 路由器。

## 功能概览

| 功能 | 说明 |
|------|------|
| 路由器管理 | 添加、编辑、删除路由器，支持 HTTP/HTTPS 双协议，连接测试与三层探测 |
| 仪表盘 | 系统概览：CPU、内存、运行时间、固件版本、存储使用率 |
| 网络接口 | 查看所有网络接口状态（up/down）、IP 地址、流量统计、DNS、网关 |
| 已连接设备 | DHCP 租约列表、设备去重（MAC 标准化）、在线状态 |
| 系统信息 | 内核版本、CPU 型号、温度、文件系统信息 |
| 诊断报告 | 生成路由器健康诊断报告 |
| 流量统计 | 接口实时流量图表 |
| 重启操作 | 10 阶段安全重启状态机，含离线等待与自动重连 |
| 密码修改 | 待定轮换模式：先设置新密码，验证后再替换本地存储凭据 |
| 证书信任 | TOFU（Trust On First Use）自签名证书指纹锁定 |

## 技术栈

| 类别 | 技术 |
|------|------|
| 语言 | Kotlin 2.1.0 |
| UI | Jetpack Compose + Material 3 (BOM 2024.12.01) |
| 架构 | MVVM + Repository + Clean Architecture |
| DI | Hilt 2.51.1 |
| 数据库 | Room 2.6.1 + DataStore |
| 网络 | OkHttp 4.12.0 + Moshi 1.15.1 |
| 异步 | Kotlin Coroutines 1.9.0 + Flow |
| 构建 | AGP 8.7.3 + KSP + Gradle Version Catalog |

## 最低要求

- **Android**: API 26 (Android 8.0) 及以上
- **路由器**: ImmortalWrt 23.05+ / OpenWrt 23.05+，需安装 `rpcd` 及相关模块

## 项目结构

```
app/src/main/java/com/immortalwrt/manager/
├── core/
│   ├── network/      # OkHttp 客户端、ubus JSON-RPC、端点探测、证书管理
│   ├── security/     # Android Keystore 密钥存储、日志脱敏
│   ├── session/      # 会话管理器（路由级 Mutex 隔离）
│   └── util/         # DebugLogger、FormatUtils
├── data/
│   ├── local/        # Room DAO/Entity、DataStore 偏好设置
│   ├── remote/       # 远程数据源（网络、设备、系统）
│   └── repository/   # Repository 实现
├── di/               # Hilt 模块（Database、Repository）
├── domain/
│   ├── model/        # 领域模型（Router、NetworkInterface、AppError 等）
│   └── repository/   # Repository 接口
└── ui/
    ├── components/   # 可复用 Compose 组件
    ├── navigation/   # 导航图与 Screen 定义
    ├── screens/      # 9 个功能页面
    │   ├── dashboard/    # 仪表盘
    │   ├── network/      # 网络接口
    │   ├── devices/      # 已连接设备
    │   ├── system/       # 系统信息
    │   ├── diagnostics/  # 诊断报告
    │   ├── traffic/      # 流量统计
    │   ├── reboot/       # 重启操作
    │   ├── password/     # 密码修改
    │   └── router/       # 路由器增删改查
    └── theme/         # Material 3 主题
```

## 构建

### 环境要求

- JDK 17+
- Android SDK 35（自动下载 build-tools、platform-tools）
- Gradle 8.x（wrapper 自带）

### 编译

```bash
# Debug APK
./gradlew assembleDebug

# Release APK（已启用 R8 混淆和资源压缩）
./gradlew assembleRelease

# 运行测试
./gradlew test

# 运行 Lint
./gradlew lint
```

### CI

GitHub Actions 工作流（`.github/workflows/ci.yml`）在每次推送和 PR 时自动执行：
- 编译 Debug APK
- 运行所有单元测试
- Lint 检查
- NoWifiGuardTest（WiFi 相关代码零容忍扫描）

## 安全设计

- **凭据存储**: 密码通过 Android Keystore（AES/GCM/NoPadding）加密存储，不落盘明文
- **日志脱敏**: `SensitiveLogSanitizer` 自动脱敏 session ID、MAC 地址、IP、密码
- **证书锁定**: HTTPS 自签名证书采用 TOFU 策略，首次信任后锁定指纹
- **HTTP 风险**: HTTP 明文连接需用户明确接受风险后方可建立
- **权限最小化**: 仅申请 `INTERNET` 和 `ACCESS_NETWORK_STATE` 权限
- **WiFi 零容忍**: CI 中包含 `NoWifiGuardTest`，禁止任何 WiFi 扫描/管理代码进入仓库

## 国际化

支持英语（`values/strings.xml`）和简体中文（`values-zh-rCN/strings.xml`），共 103 条字符串资源。

## 版本

当前版本: **0.1.0-alpha** (versionCode 100)

## 许可证

本项目仅供个人学习和研究使用。

## 相关文档

- [PRD](docs/PRD.md) — 产品需求文档
- [MVP 范围](docs/MVP_SCOPE.md) — 最小可行产品范围
- [超出范围](docs/OUT_OF_SCOPE.md) — 明确不做的功能
- [API 兼容性](docs/API_COMPATIBILITY.md) — 路由器端 API 验证文档
- [安全设计](docs/SECURITY.md) — 安全架构详情
- [隐私说明](docs/PRIVACY.md) — 隐私保护措施
