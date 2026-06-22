package com.dodesoft.sdk.resources

import com.dodesoft.sdk.DodeSoftClient
import io.ktor.client.request.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

class AccountResource(private val client: DodeSoftClient) {

    suspend fun balance(): JsonObject =
        client.get("/balance/")

    suspend fun balanceByCurrency(since: Long? = null): JsonElement =
        client.get("/account/json/balance/") {
            since?.let { parameter("from", it) }
        }

    suspend fun turnover(): JsonElement =
        client.get("/account/json/turnover/")

    suspend fun createStatement(request: JsonObject): JsonObject =
        client.post("/company_statements/") {
            io.ktor.http.contentType(io.ktor.http.ContentType.Application.Json)
            setBody(request.toString())
        }

    suspend fun listStatements(): List<JsonObject> =
        client.get("/company_statements/")

    suspend fun getStatement(id: String): JsonObject =
        client.get("/company_statements/$id/")

    suspend fun cancelStatement(id: String): JsonObject =
        client.post("/company_statements/$id/cancel/")
}
