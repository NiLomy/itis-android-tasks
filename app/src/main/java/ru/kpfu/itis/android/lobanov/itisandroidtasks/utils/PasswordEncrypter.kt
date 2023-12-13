package ru.kpfu.itis.android.lobanov.itisandroidtasks.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object PasswordEncrypter {
    private const val BYTE_FORMATTING = "%02x"

    fun encrypt(password: String, encryptingAlgorithm: String): String {
        val md = MessageDigest.getInstance(encryptingAlgorithm)
        val hashBytes = md.digest(password.toByteArray(StandardCharsets.UTF_8))
        return with(StringBuilder()) {
            hashBytes.forEach { byte ->
                append(String.format(BYTE_FORMATTING, byte))
            }
            toString().lowercase()
        }
    }
}
