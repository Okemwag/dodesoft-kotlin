package com.dodesoft.sdk

sealed class DodeSoftException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    data class ApiException(
        val statusCode: Int,
        val code: String?,
        val errorMessage: String?,
        val raw: String,
    ) : DodeSoftException("DodeSoft API error $statusCode${if (code != null) " [$code]" else ""}: $errorMessage")

    data class NetworkException(
        override val cause: Throwable,
    ) : DodeSoftException("Network error: ${cause.message}", cause)

    data class SerializationException(
        override val cause: Throwable,
        val raw: String,
    ) : DodeSoftException("Failed to deserialize response: ${cause.message}", cause)

    class SignatureException(message: String) : DodeSoftException(message)
}
