package com.dodesoft.sdk.resources

import com.dodesoft.sdk.DodeSoftClient
import com.dodesoft.sdk.models.*
import io.ktor.client.request.*
import io.ktor.http.*

class PurchasesResource(private val client: DodeSoftClient) {

    suspend fun create(request: CreatePurchaseRequest): Purchase =
        client.post("/purchases/") {
            contentType(ContentType.Application.Json)
            setBody(client.json.encodeToString(CreatePurchaseRequest.serializer(), request))
        }

    suspend fun get(id: String): Purchase =
        client.get("/purchases/$id/")

    suspend fun cancel(id: String): Purchase =
        client.post("/purchases/$id/cancel/")

    suspend fun capture(id: String, request: CaptureRequest = CaptureRequest()): Purchase =
        client.post("/purchases/$id/capture/") {
            contentType(ContentType.Application.Json)
            setBody(client.json.encodeToString(CaptureRequest.serializer(), request))
        }

    suspend fun release(id: String): Purchase =
        client.post("/purchases/$id/release/")

    suspend fun charge(id: String, request: ChargeRequest): Purchase =
        client.post("/purchases/$id/charge/") {
            contentType(ContentType.Application.Json)
            setBody(client.json.encodeToString(ChargeRequest.serializer(), request))
        }

    suspend fun deleteRecurringToken(id: String): Purchase =
        client.post("/purchases/$id/delete_recurring_token/")

    /**
     * Initiates a full or partial refund for a paid purchase.
     * Set [RefundRequest.amount] in minor units for partial refunds; leave null for full.
     * Verify [Purchase.refundAvailability] and [Purchase.refundableAmount] before calling.
     */
    suspend fun refund(id: String, request: RefundRequest = RefundRequest()): Payment =
        client.post("/purchases/$id/refund/") {
            contentType(ContentType.Application.Json)
            setBody(client.json.encodeToString(RefundRequest.serializer(), request))
        }

    /**
     * Manually marks a purchase as paid.
     * For admin/operational use only — do not call from automated checkout flows.
     */
    suspend fun markAsPaid(id: String, request: MarkAsPaidRequest): Purchase =
        client.post("/purchases/$id/mark_as_paid/") {
            contentType(ContentType.Application.Json)
            setBody(client.json.encodeToString(MarkAsPaidRequest.serializer(), request))
        }

    suspend fun resendInvoice(id: String): Purchase =
        client.post("/purchases/$id/resend_invoice/")
}
