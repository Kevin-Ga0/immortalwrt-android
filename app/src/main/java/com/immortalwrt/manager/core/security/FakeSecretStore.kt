package com.immortalwrt.manager.core.security

import java.util.concurrent.ConcurrentHashMap

class FakeSecretStore : SecretStore {

    private val secrets = ConcurrentHashMap<String, ByteArray>()

    override suspend fun store(alias: String, secret: ByteArray) {
        secrets[alias] = secret
    }

    override suspend fun retrieve(alias: String): ByteArray? = secrets[alias]

    override suspend fun delete(alias: String) {
        secrets.remove(alias)
    }

    override suspend fun clearAll() {
        secrets.clear()
    }

    override suspend fun contains(alias: String): Boolean = secrets.containsKey(alias)
}
