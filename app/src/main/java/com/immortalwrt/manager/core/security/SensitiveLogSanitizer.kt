package com.immortalwrt.manager.core.security

object SensitiveLogSanitizer {

    private val macPattern = Regex("""([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})""")
    private val ipPattern = Regex("""\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b""")
    private val sessionIdPattern = Regex("""\b[a-fA-F0-9]{32,64}\b""")
    private val uuidPattern = Regex(
        """\b[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\b"""
    )

    fun sanitizeSessionId(sessionId: String): String {
        if (sessionId.length <= 8) return "*".repeat(sessionId.length)
        return sessionId.take(4) + "*".repeat(sessionId.length - 8) + sessionId.takeLast(4)
    }

    fun sanitizePassword(text: String): String = "******"

    fun sanitizeMac(mac: String): String {
        val separator = if (mac.contains("-")) "-" else ":"
        val parts = mac.split(separator)
        if (parts.size < 2) return "**${separator}**${separator}**${separator}**${separator}**${separator}**"
        return parts.take(2).joinToString(separator) + separator + listOf("**", "**", "**", "**").joinToString(
            separator
        )
    }

    fun sanitizeIp(ip: String): String {
        val lastDot = ip.lastIndexOf('.')
        if (lastDot == -1) return "xxx.xxx.xxx.xxx"
        return ip.substring(0, lastDot) + ".xxx"
    }

    fun sanitizeHostname(hostname: String): String = "<hidden>"

    fun sanitizeAuthHeader(header: String): String = "******"

    fun redactSensitiveFields(text: String): String {
        var result = text
        result = result.replace(macPattern) { sanitizeMac(it.value) }
        result = result.replace(ipPattern) { sanitizeIp(it.value) }
        result = result.replace(sessionIdPattern) { sanitizeSessionId(it.value) }
        result = result.replace(uuidPattern) { sanitizeSessionId(it.value) }
        result = result.replace(Regex(""""password"\s*:\s*"[^"]*"""", RegexOption.IGNORE_CASE)) {
            """"password": "******""""
        }
        result = result.replace(Regex(""""ubus_rpc_session"\s*:\s*"[^"]*"""")) {
            """"ubus_rpc_session": "******""""
        }
        return result
    }

    fun sanitizeRequestBodyForLogging(body: String): String {
        var result = body
        result = result.replace(
            Regex(""""password"\s*:\s*"[^"]*""""),
            """"password": "******""""
        )
        result = result.replace(
            Regex(""""ubus_rpc_session"\s*:\s*"[^"]*""""),
            """"ubus_rpc_session": "******""""
        )
        result = result.replace(
            Regex(""""password"\s*:\s*[^,\s}]"""),
            """"password": "****""""
        )
        return result
    }
}
