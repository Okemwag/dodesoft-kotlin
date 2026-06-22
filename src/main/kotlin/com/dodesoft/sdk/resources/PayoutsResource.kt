package com.dodesoft.sdk.resources

import com.dodesoft.sdk.DodeSoftClient
import com.dodesoft.sdk.models.*
import io.ktor.client.request.*
import io.ktor.http.*

class PayoutsResource(private val client: DodeSoftClient) {

    suspend fun listMethods(
        brandId: String,
        currency: String,
        language: String? = null,
    ): PaymentMethodsResponse = client.get("/payout_methods/") {
        parameter("brand_id", brandId)
        parameter("currency", currency)
        language?.let { parameter("language", it) }
    }

    suspend fun create(request: CreatePayoutRequest): Payout =
        client.post("/payouts/") {
            contentType(ContentType.Application.Json)
            setBody(client.json.encodeToString(CreatePayoutRequest.serializer(), request))
        }

    suspend fun get(id: String): Payout =
        client.get("/payouts/$id/")
}
