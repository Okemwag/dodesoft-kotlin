package com.dodesoft.sdk.resources

import com.dodesoft.sdk.DodeSoftClient
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * Provides access to /billing/ and /billing_templates/ endpoints (Batch 3).
 * Not required for standard hosted-checkout flows.
 */
class BillingResource(private val client: DodeSoftClient) {

    suspend fun createInvoices(request: JsonObject): JsonElement =
        client.post("/billing/") {
            contentType(ContentType.Application.Json)
            setBody(request.toString())
        }

    suspend fun createTemplate(request: JsonObject): JsonElement =
        client.post("/billing_templates/") {
            contentType(ContentType.Application.Json)
            setBody(request.toString())
        }

    suspend fun listTemplates(): List<JsonObject> =
        client.get("/billing_templates/")

    suspend fun getTemplate(id: String): JsonObject =
        client.get("/billing_templates/$id/")

    suspend fun updateTemplate(id: String, request: JsonObject): JsonElement =
        client.put("/billing_templates/$id/") {
            contentType(ContentType.Application.Json)
            setBody(request.toString())
        }

    suspend fun deleteTemplate(id: String) = client.delete("/billing_templates/$id/")

    suspend fun sendInvoice(templateId: String, request: JsonObject): JsonElement =
        client.post("/billing_templates/$templateId/send_invoice/") {
            contentType(ContentType.Application.Json)
            setBody(request.toString())
        }

    suspend fun addSubscriber(templateId: String, request: JsonObject): JsonElement =
        client.post("/billing_templates/$templateId/add_subscriber/") {
            contentType(ContentType.Application.Json)
            setBody(request.toString())
        }

    suspend fun listSubscribers(templateId: String): List<JsonObject> =
        client.get("/billing_templates/$templateId/clients/")

    suspend fun getSubscriber(templateId: String, clientId: String): JsonObject =
        client.get("/billing_templates/$templateId/clients/$clientId/")

    suspend fun updateSubscriber(templateId: String, clientId: String, request: JsonObject): JsonElement =
        client.patch("/billing_templates/$templateId/clients/$clientId/") {
            contentType(ContentType.Application.Json)
            setBody(request.toString())
        }
}
