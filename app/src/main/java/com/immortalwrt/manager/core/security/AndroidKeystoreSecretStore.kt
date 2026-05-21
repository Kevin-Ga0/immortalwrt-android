package com.immortalwrt.manager.core.security

import android.content.Context
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties

@Singleton
class AndroidKeystoreSecretStore @Inject constructor(
    @ApplicationContext private val context: Context
) : SecretStore {

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    private val sharedPrefs = context.getSharedPreferences("iwrt_secure_prefs", Context.MODE_PRIVATE)
    private val keyAliasPrefix = "iwrt_mgr_"

    override suspend fun store(alias: String, secret: ByteArray) {
        val key = getOrCreateKey(alias)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val ciphertext = cipher.doFinal(secret)
        val iv = cipher.iv
        val combined = iv + ciphertext
        sharedPrefs.edit().putString(alias, Base64.encodeToString(combined, Base64.NO_WRAP)).apply()
    }

    override suspend fun retrieve(alias: String): ByteArray? {
        val encoded = sharedPrefs.getString(alias, null) ?: return null
        val combined = Base64.decode(encoded, Base64.NO_WRAP)
        val key = getOrCreateKey(alias)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val iv = combined.copyOfRange(0, 12)
        val ciphertext = combined.copyOfRange(12, combined.size)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        return cipher.doFinal(ciphertext)
    }

    override suspend fun delete(alias: String) {
        val keyAlias = keyAliasPrefix + alias
        if (keyStore.containsAlias(keyAlias)) {
            keyStore.deleteEntry(keyAlias)
        }
        sharedPrefs.edit().remove(alias).apply()
    }

    override suspend fun clearAll() {
        val aliases = keyStore.aliases()
        while (aliases.hasMoreElements()) {
            val alias = aliases.nextElement()
            if (alias.startsWith(keyAliasPrefix)) {
                keyStore.deleteEntry(alias)
            }
        }
        sharedPrefs.edit().clear().apply()
    }

    override suspend fun contains(alias: String): Boolean = sharedPrefs.contains(alias)

    private fun getOrCreateKey(alias: String): SecretKey {
        val keyAlias = keyAliasPrefix + alias
        if (keyStore.containsAlias(keyAlias)) {
            return keyStore.getKey(keyAlias, null) as SecretKey
        }
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val spec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }
}
