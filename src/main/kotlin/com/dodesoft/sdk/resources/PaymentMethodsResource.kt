package com.dodesoft.sdk.resources

import com.dodesoft.sdk.DodeSoftClient
import com.dodesoft.sdk.models.PaymentMethodsResponse
import io.ktor.client.request.*

class PaymentMethodsResource(private val client: DodeSoftClient) {

    suspend fun list(
        brandId: String,
        currency: String,
        country: String? = null,
        recurring: Boolean? = null,
        skipCapture: Boolean? = null,
        preauthorization: Boolean? = null,
        language: String? = null,
    ): PaymentMethodsResponse = client.get("/payment_methods/") {
        parameter("brand_id", brandId)
        parameter("currency", currency)
        country?.let { parameter("country", it) }
        recurring?.let { parameter("recurring", it) }
        skipCapture?.let { parameter("skip_capture", it) }
        preauthorization?.let { parameter("preauthorization", it) }
        language?.let { parameter("language", it) }
    }
}
