package com.dodesoft.sdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

enum class WebhookEvent(val value: String) {
    PURCHASE_CREATED("purchase.created"),
    PURCHASE_PAID("purchase.paid"),
    PURCHASE_PAYMENT_FAILURE("purchase.payment_failure"),
    PURCHASE_REFUND_FAILURE("purchase.refund_failure"),
    PURCHASE_CAPTURE_FAILURE("purchase.capture_failure"),
    PURCHASE_RELEASE_FAILURE("purchase.release_failure"),
    PURCHASE_PENDING_EXECUTE("purchase.pending_execute"),
    PURCHASE_PENDING_CHARGE("purchase.pending_charge"),
    PURCHASE_CANCELLED("purchase.cancelled"),
    PURCHASE_HOLD("purchase.hold"),
    PURCHASE_CAPTURED("purchase.captured"),
    PURCHASE_PENDING_CAPTURE("purchase.pending_capture"),
    PURCHASE_RELEASED("purchase.released"),
    PURCHASE_PENDING_RELEASE("purchase.pending_release"),
    PURCHASE_PREAUTHORIZED("purchase.preauthorized"),
    PURCHASE_PENDING_RECURRING_TOKEN_DELETE("purchase.pending_recurring_token_delete"),
    PURCHASE_RECURRING_TOKEN_DELETED("purchase.recurring_token_deleted"),
    PURCHASE_SUBSCRIPTION_CHARGE_FAILURE("purchase.subscription_charge_failure"),
    PURCHASE_PENDING_REFUND("purchase.pending_refund"),
    PAYMENT_REFUNDED("payment.refunded"),
    BILLING_CLIENT_SUBSCRIPTION_CANCELLED("billing_template_client.subscription_billing_cancelled"),
    PAYOUT_PENDING("payout.pending"),
    PAYOUT_FAILED("payout.failed"),
    PAYOUT_SUCCESS("payout.success"),
    PAYMENT_CHARGED_BACK("payment.charged_back"),
    PURCHASE_VIEWED("purchase.viewed"),
    PURCHASE_SETTLED("purchase.settled"),
    PAYOUT_CREATED("payout.created"),
    PAYMENT_CHARGEBACK_REVERSED("payment.chargeback_reversed"),
}

@Serializable
data class Webhook(
    val id: String,
    val type: String = "",
    @SerialName("created_on") val createdOn: Long = 0,
    @SerialName("updated_on") val updatedOn: Long = 0,
    val title: String = "",
    val callback: String = "",
    val events: List<String> = emptyList(),
    @SerialName("all_events") val allEvents: Boolean = false,
    @SerialName("public_key") val publicKey: String? = null,
)

@Serializable
data class WebhookDeliveryAttempt(
    @SerialName("attempted_on") val attemptedOn: String,
    @SerialName("error_message") val errorMessage: String? = null,
)

@Serializable
data class WebhookDelivery(
    @SerialName("created_on") val createdOn: String,
    @SerialName("delivered_on") val deliveredOn: String? = null,
    val attempts: Int = 0,
    @SerialName("delivery_attempts") val deliveryAttempts: List<WebhookDeliveryAttempt> = emptyList(),
    val url: String = "",
    val event: String = "",
    val payload: JsonElement? = null,
)

@Serializable
data class WebhookPayload(
    @SerialName("event_type") val eventType: String,
    val data: JsonElement? = null,
)

@Serializable
data class CreateWebhookRequest(
    val title: String,
    val callback: String,
    val events: List<String>? = null,
    @SerialName("all_events") val allEvents: Boolean = false,
)

@Serializable
data class PatchWebhookRequest(
    val title: String? = null,
    val callback: String? = null,
    val events: List<String>? = null,
    @SerialName("all_events") val allEvents: Boolean? = null,
)
