package com.dodesoft.sdk.resources

import com.dodesoft.sdk.DodeSoftClient
import com.dodesoft.sdk.models.ClientDetails
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonObject

class ClientsResource(private val client: DodeSoftClient) {

    suspend fun create(details: ClientDetails): JsonObject =
        client.post("/clients/") {
            contentType(ContentType.Application.Json)
            setBody(client.json.encodeToString(ClientDetails.serializer(), details))
        }

    suspend fun list(): List<JsonObject> =
        client.get("/clients/")

    suspend fun get(id: String): JsonObject =
        client.get("/clients/$id/")

    suspend fun update(id: String, details: ClientDetails): JsonObject =
        client.put("/clients/$id/") {
            contentType(ContentType.Application.Json)
            setBody(client.json.encodeToString(ClientDetails.serializer(), details))
        }

    suspend fun patch(id: String, details: ClientDetails): JsonObject =
        client.patch("/clients/$id/") {
            contentType(ContentType.Application.Json)
            setBody(client.json.encodeToString(ClientDetails.serializer(), details))
        }

    suspend fun delete(id: String) = client.delete("/clients/$id/")

    suspend fun listRecurringTokens(clientId: String): List<JsonObject> =
        client.get("/clients/$clientId/recurring_tokens/")

    suspend fun getRecurringToken(clientId: String, tokenId: String): JsonObject =
        client.get("/clients/$clientId/recurring_tokens/$tokenId/")

    suspend fun deleteRecurringToken(clientId: String, tokenId: String) =
        client.delete("/clients/$clientId/recurring_tokens/$tokenId/")
}
