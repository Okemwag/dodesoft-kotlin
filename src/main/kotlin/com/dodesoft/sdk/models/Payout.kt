package com.dodesoft.sdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class PayoutStatus(val value: String) {
    INITIALIZED("initialized"),
    PENDING("pending"),
    ERROR("error"),
    SUCCESS("success"),
}

@Serializable
data class PayoutStatusEntry(
    val status: String,
    val timestamp: Long,
)

@Serializable
data class Payout(
    val id: String,
    val type: String = "",
    @SerialName("created_on") val createdOn: Long = 0,
    @SerialName("updated_on") val updatedOn: Long = 0,
    val payment: PaymentDetails? = null,
    val client: ClientDetails? = null,
    @SerialName("transaction_data") val transactionData: TransactionData? = null,
    val status: String = "",
    @SerialName("status_history") val statusHistory: List<PayoutStatusEntry> = emptyList(),
    @SerialName("brand_id") val brandId: String = "",
    val reference: String = "",
    @SerialName("is_test") val isTest: Boolean = false,
    @SerialName("execution_url") val executionUrl: String? = null,
) {
    val payoutStatus: PayoutStatus?
        get() = PayoutStatus.entries.find { it.value == status }
}

@Serializable
data class CreatePayoutRequest(
    val payment: PaymentDetails,
    val client: ClientDetails,
    @SerialName("brand_id") val brandId: String,
    val reference: String? = null,
    @SerialName("payout_method_whitelist") val payoutMethodWhitelist: List<String>? = null,
)
