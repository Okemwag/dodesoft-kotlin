package com.dodesoft.sdk.resources

import com.dodesoft.sdk.DodeSoftClient
import com.dodesoft.sdk.DodeSoftException
import com.dodesoft.sdk.models.*
import com.dodesoft.sdk.signature.SignatureVerifier
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class WebhooksResource(private val client: DodeSoftClient) {

    suspend fun create(request: CreateWebhookRequest): Webhook =
        client.post("/webhooks/") {
            contentType(ContentType.Application.Json)
            setBody(client.json.encodeToString(CreateWebhookRequest.serializer(), request))
        }

    suspend fun list(): List<Webhook> =
        client.get("/webhooks/")

    suspend fun get(id: String): Webhook =
        client.get("/webhooks/$id/")

    suspend fun update(id: String, request: CreateWebhookRequest): Webhook =
        client.put("/webhooks/$id/") {
            contentType(ContentType.Application.Json)
            setBody(client.json.encodeToString(CreateWebhookRequest.serializer(), request))
        }

    suspend fun patch(id: String, request: PatchWebhookRequest): Webhook =
        client.patch("/webhooks/$id/") {
            contentType(ContentType.Application.Json)
            setBody(client.json.encodeToString(PatchWebhookRequest.serializer(), request))
        }

    suspend fun delete(id: String) = client.delete("/webhooks/$id/")

    suspend fun deliveries(id: String, sourceType: String): List<WebhookDelivery> =
        client.get("/webhooks/deliveries/") {
            parameter("id", id)
            parameter("source_type", sourceType)
        }

    suspend fun getPublicKey(): PublicKeyResponse =
        client.get("/public_key/")

    /**
     * Verifies [signatureBase64] against [rawBody] and returns a parsed [WebhookPayload].
     * Throws [DodeSoftException.SignatureException] if verification fails.
     */
    fun parseVerified(
        rawBody: ByteArray,
        signatureBase64: String,
        publicKeyPem: String,
    ): WebhookPayload {
        SignatureVerifier.verifyOrThrow(rawBody, signatureBase64, publicKeyPem)
        return try {
            client.json.decodeFromString(WebhookPayload.serializer(), rawBody.decodeToString())
        } catch (e: Exception) {
            throw DodeSoftException.SerializationException(e, rawBody.decodeToString())
        }
    }
}
