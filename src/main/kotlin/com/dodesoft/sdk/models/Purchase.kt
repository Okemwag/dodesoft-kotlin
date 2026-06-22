package com.dodesoft.sdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

enum class PurchaseStatus(val value: String) {
    CREATED("created"),
    SENT("sent"),
    VIEWED("viewed"),
    ERROR("error"),
    CANCELLED("cancelled"),
    OVERDUE("overdue"),
    EXPIRED("expired"),
    HOLD("hold"),
    RELEASED("released"),
    PENDING_RELEASE("pending_release"),
    PENDING_CAPTURE("pending_capture"),
    PREAUTHORIZED("preauthorized"),
    PAID("paid"),
    PENDING_EXECUTE("pending_execute"),
    PENDING_CHARGE("pending_charge"),
    CHARGEBACK("chargeback"),
    PENDING_REFUND("pending_refund"),
    REFUNDED("refunded");

    val isFinal: Boolean get() = this in setOf(PAID, CANCELLED, EXPIRED, REFUNDED, RELEASED)
    val isPending: Boolean get() = this in setOf(
        PENDING_EXECUTE, PENDING_CAPTURE, PENDING_RELEASE, PENDING_CHARGE, PENDING_REFUND
    )
}

enum class RefundAvailability(val value: String) {
    ALL("all"),
    FULL_ONLY("full_only"),
    PARTIAL_ONLY("partial_only"),
    PIS_ALL("pis_all"),
    PIS_PARTIAL("pis_partial"),
    NONE("none"),
}

@Serializable
data class PurchaseDetails(
    val products: List<Product>,
    val currency: String? = null,
    val total: Long? = null,
    val language: String? = null,
    val notes: String? = null,
    val metadata: Map<String, JsonElement>? = null,
    @SerialName("single_attempt") val singleAttempt: Boolean = false,
)

@Serializable
data class Purchase(
    val id: String,
    val type: String = "",
    @SerialName("created_on") val createdOn: Long = 0,
    @SerialName("updated_on") val updatedOn: Long = 0,
    val client: ClientDetails? = null,
    val purchase: PurchaseDetails? = null,
    val payment: PaymentDetails? = null,
    @SerialName("transaction_data") val transactionData: TransactionData? = null,
    val status: String = "",
    @SerialName("status_history") val statusHistory: List<StatusEntry> = emptyList(),
    @SerialName("brand_id") val brandId: String = "",
    @SerialName("client_id") val clientId: String? = null,
    @SerialName("billing_template_id") val billingTemplateId: String? = null,
    val reference: String = "",
    @SerialName("is_test") val isTest: Boolean = false,
    @SerialName("is_recurring_token") val isRecurringToken: Boolean = false,
    @SerialName("recurring_token") val recurringToken: String? = null,
    @SerialName("skip_capture") val skipCapture: Boolean = false,
    @SerialName("force_recurring") val forceRecurring: Boolean = false,
    @SerialName("send_receipt") val sendReceipt: Boolean = false,
    @SerialName("marked_as_paid") val markedAsPaid: Boolean = false,
    @SerialName("refund_availability") val refundAvailability: String? = null,
    @SerialName("refundable_amount") val refundableAmount: Long? = null,
    @SerialName("checkout_url") val checkoutUrl: String = "",
    @SerialName("direct_post_url") val directPostUrl: String? = null,
    @SerialName("invoice_url") val invoiceUrl: String? = null,
    @SerialName("success_redirect") val successRedirect: String? = null,
    @SerialName("failure_redirect") val failureRedirect: String? = null,
    @SerialName("cancel_redirect") val cancelRedirect: String? = null,
    @SerialName("success_callback") val successCallback: String? = null,
) {
    val purchaseStatus: PurchaseStatus?
        get() = PurchaseStatus.entries.find { it.value == status }
}

@Serializable
data class Payment(
    val id: String,
    val type: String = "",
    @SerialName("created_on") val createdOn: Long = 0,
    @SerialName("updated_on") val updatedOn: Long = 0,
    val client: ClientDetails? = null,
    val payment: PaymentDetails? = null,
    @SerialName("transaction_data") val transactionData: TransactionData? = null,
    @SerialName("related_to") val relatedTo: RelatedObject? = null,
    val reference: String = "",
    @SerialName("is_test") val isTest: Boolean = false,
    @SerialName("brand_id") val brandId: String = "",
)

// ─── Request types ────────────────────────────────────────────────────────────

@Serializable
data class CreatePurchaseRequest(
    val purchase: PurchaseDetails,
    @SerialName("brand_id") val brandId: String,
    val client: ClientDetails? = null,
    @SerialName("client_id") val clientId: String? = null,
    val reference: String? = null,
    @SerialName("success_redirect") val successRedirect: String? = null,
    @SerialName("failure_redirect") val failureRedirect: String? = null,
    @SerialName("cancel_redirect") val cancelRedirect: String? = null,
    @SerialName("success_callback") val successCallback: String? = null,
    @SerialName("send_receipt") val sendReceipt: Boolean = false,
    @SerialName("skip_capture") val skipCapture: Boolean = false,
    @SerialName("is_recurring_token") val isRecurringToken: Boolean = false,
    @SerialName("force_recurring") val forceRecurring: Boolean = false,
    @SerialName("payment_method_whitelist") val paymentMethodWhitelist: List<String>? = null,
)

@Serializable
data class CaptureRequest(val amount: Long? = null)

@Serializable
data class ChargeRequest(@SerialName("recurring_token") val recurringToken: String)

@Serializable
data class RefundRequest(
    val amount: Long? = null,
    val reference: String? = null,
)

@Serializable
data class MarkAsPaidRequest(@SerialName("paid_on") val paidOn: Long)
