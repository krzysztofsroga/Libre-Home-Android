package com.krzysztofsroga.librehome.utils

import android.util.Base64
import java.security.KeyPair
import java.security.KeyPairGenerator

object SshKeygen {
    // https://stackoverflow.com/questions/4932005/can-we-use-jsch-for-ssh-key-based-communication
    fun generate(): String {
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(2048)
        val keyPair: KeyPair = kpg.genKeyPair()
        val privateKey: String = Base64.encodeToString(keyPair.private.encoded, Base64.DEFAULT)
        val publicKey: String = Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT)
        return """
            -----BEGIN RSA PRIVATE KEY-----
            $publicKey
            -----END RSA PRIVATE KEY-----
        """.trimIndent()
    }
}