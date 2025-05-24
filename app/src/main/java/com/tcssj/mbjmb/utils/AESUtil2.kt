package com.tcssj.mbjmb.utils

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.nio.charset.StandardCharsets
import java.util.Base64

object AESUtil2 {
    fun base64ToDecode(str: String): ByteArray {
        return Base64.getDecoder().decode(str)
    }

    fun encode(key: ByteArray): String {
        return Base64.getEncoder().encodeToString(key)
    }

    fun encrypt(content: String, key: String): String {
        return encrypt(content, key.toByteArray())
    }

    fun encrypt(content: String, key: ByteArray): String {
        return try {
            val skey = SecretKeySpec(key, "utf-8")
            val iv = IvParameterSpec(key, 0, 16)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val byteContent = content.toByteArray(StandardCharsets.UTF_8)
            cipher.init(Cipher.ENCRYPT_MODE, skey, iv)
            val result = cipher.doFinal(byteContent)
            encode(result)
        } catch (e: Exception) {
            e.printStackTrace()
            content
        }
    }

    fun decrypt(content: String, key: ByteArray): String {
        return try {
            val skey = SecretKeySpec(key, "utf-8")
            val iv = IvParameterSpec(key, 0, 16)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, skey, iv)
            val result = cipher.doFinal(base64ToDecode(content))
            String(result)
        } catch (e: Exception) {
            content
        }
    }
} 