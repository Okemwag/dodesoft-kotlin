package com.dodesoft.sdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientDetails(
    val email: String,
    val phone: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("personal_code") val personalCode: String? = null,
    @SerialName("street_address") val streetAddress: String? = null,
    val country: String? = null,
    val city: String? = null,
    @SerialName("zip_code") val zipCode: String? = null,
    val state: String? = null,
    @SerialName("legal_name") val legalName: String? = null,
    @SerialName("brand_name") val brandName: String? = null,
)

@Serializable
data class Product(
    val name: String,
    val price: Long,
    val quantity: String? = null,
    val discount: Long? = null,
    @SerialName("tax_percent") val taxPercent: String? = null,
    val category: String? = null,
)

@Serializable
data class PaymentDetails(
    @SerialName("is_outgoing") val isOutgoing: Boolean = false,
    @SerialName("payment_type") val paymentType: String? = null,
    val amount: Long = 0,
    val currency: String = "",
    @SerialName("net_amount") val netAmount: Long? = null,
    @SerialName("fee_amount") val feeAmount: Long? = null,
    @SerialName("paid_on") val paidOn: Long? = null,
)

@Serializable
data class StatusEntry(
    val status: String,
    val timestamp: Long,
)

@Serializable
data class TransactionAttemptError(
    val code: String,
    val message: String? = null,
)

@Serializable
data class TransactionAttempt(
    @SerialName("created_on") val createdOn: Long = 0,
    val error: TransactionAttemptError? = null,
)

@Serializable
data class TransactionData(
    @SerialName("payment_method") val paymentMethod: String? = null,
    val attempts: List<TransactionAttempt> = emptyList(),
)

@Serializable
data class RelatedObject(
    val type: String,
    val id: String,
)

@Serializable
data class PublicKeyResponse(
    @SerialName("public_key") val publicKey: String,
)

@Serializable
data class PaymentMethodsResponse(
    @SerialName("available_payment_methods") val availablePaymentMethods: List<String> = emptyList(),
)
