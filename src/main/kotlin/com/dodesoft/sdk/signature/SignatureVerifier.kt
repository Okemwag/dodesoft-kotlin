package com.dodesoft.sdk.signature

import com.dodesoft.sdk.DodeSoftException
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

/**
 * Verifies the X-Signature header on DodeSoft callbacks and webhooks.
 *
 * The signature is a base64-encoded RSA PKCS#1 v1.5 signature over the SHA-256
 * digest of the exact raw request body bytes.
 */
object SignatureVerifier {

    /**
     * Returns true if [signatureBase64] is a valid RSA-SHA256 PKCS#1v1.5 signature
     * over [rawBody] using [publicKeyPem].
     *
     * @throws DodeSoftException.SignatureException on invalid key or malformed input.
     */
    fun verify(rawBody: ByteArray, signatureBase64: String, publicKeyPem: String): Boolean {
        require(rawBody.isNotEmpty()) { "rawBody must not be empty" }
        require(signatureBase64.isNotBlank()) { "signatureBase64 must not be blank" }
        require(publicKeyPem.isNotBlank()) { "publicKeyPem must not be blank" }

        val sigBytes = try {
            Base64.getDecoder().decode(signatureBase64)
        } catch (e: IllegalArgumentException) {
            throw DodeSoftException.SignatureException("Invalid base64 signature: ${e.message}")
        }

        val pubKey = try {
            val pemBody = publicKeyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("-----BEGIN RSA PUBLIC KEY-----", "")
                .replace("-----END RSA PUBLIC KEY-----", "")
                .replace("\\s".toRegex(), "")
            val keyBytes = Base64.getDecoder().decode(pemBody)
            val spec = X509EncodedKeySpec(keyBytes)
            KeyFactory.getInstance("RSA").generatePublic(spec)
        } catch (e: Exception) {
            throw DodeSoftException.SignatureException("Failed to parse RSA public key: ${e.message}")
        }

        return try {
            Signature.getInstance("SHA256withRSA").run {
                initVerify(pubKey)
                update(rawBody)
                verify(sigBytes)
            }
        } catch (e: Exception) {
            throw DodeSoftException.SignatureException("Signature verification error: ${e.message}")
        }
    }

    /** Throws [DodeSoftException.SignatureException] if verification fails. */
    fun verifyOrThrow(rawBody: ByteArray, signatureBase64: String, publicKeyPem: String) {
        if (!verify(rawBody, signatureBase64, publicKeyPem)) {
            throw DodeSoftException.SignatureException("Signature verification failed")
        }
    }
}
