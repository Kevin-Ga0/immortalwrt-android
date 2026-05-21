package com.immortalwrt.manager.core.security

interface SecretStore {
    suspend fun store(alias: String, secret: ByteArray)
    suspend fun retrieve(alias: String): ByteArray?
    suspend fun delete(alias: String)
    suspend fun clearAll()
    suspend fun contains(alias: String): Boolean
}
