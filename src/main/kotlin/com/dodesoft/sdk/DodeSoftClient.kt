package com.dodesoft.sdk

import com.dodesoft.sdk.resources.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Entry point for the DodeSoft API.
 *
 * Usage:
 * ```kotlin
 * val client = DodeSoftClient(DodeSoftConfig(apiKey = System.getenv("DODESOFT_API_KEY")))
 * val purchase = client.purchases.create(CreatePurchaseRequest(...))
 * client.close()
 * ```
 *
 * The client is [Closeable] — close it when the application shuts down to release connections.
 */
class DodeSoftClient(private val config: DodeSoftConfig) : AutoCloseable {

    internal val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
    }

    internal val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            level = LogLevel.NONE
        }
        defaultRequest {
            url(config.baseUrl + "/")
            header(HttpHeaders.Authorization, "Bearer ${config.apiKey}")
            header(HttpHeaders.Accept, ContentType.Application.Json.toString())
        }
        expectSuccess = false
    }

    val purchases = PurchasesResource(this)
    val paymentMethods = PaymentMethodsResource(this)
    val payouts = PayoutsResource(this)
    val clients = ClientsResource(this)
    val webhooks = WebhooksResource(this)
    val billing = BillingResource(this)
    val account = AccountResource(this)

    override fun close() = httpClient.close()

    // ─── Internal HTTP helpers ────────────────────────────────────────────────

    internal suspend inline fun <reified T> get(path: String, block: HttpRequestBuilder.() -> Unit = {}): T =
        execute(HttpMethod.Get, path, block)

    internal suspend inline fun <reified T> post(path: String, noinline block: HttpRequestBuilder.() -> Unit = {}): T =
        execute(HttpMethod.Post, path, block)

    internal suspend inline fun <reified T> put(path: String, noinline block: HttpRequestBuilder.() -> Unit = {}): T =
        execute(HttpMethod.Put, path, block)

    internal suspend inline fun <reified T> patch(path: String, noinline block: HttpRequestBuilder.() -> Unit = {}): T =
        execute(HttpMethod.Patch, path, block)

    internal suspend fun delete(path: String) {
        val response = httpClient.delete(config.baseUrl + path)
        if (!response.status.isSuccess()) throwApiException(response)
    }

    internal suspend inline fun <reified T> execute(
        method: HttpMethod,
        path: String,
        crossinline block: HttpRequestBuilder.() -> Unit,
    ): T {
        val response = httpClient.request(config.baseUrl + path) {
            this.method = method
            block()
        }
        if (!response.status.isSuccess()) throwApiException(response)
        val raw = response.bodyAsText()
        return try {
            json.decodeFromString(raw)
        } catch (e: Exception) {
            throw DodeSoftException.SerializationException(e, raw)
        }
    }

    internal suspend fun throwApiException(response: HttpResponse): Nothing {
        val raw = response.bodyAsText()
        val code: String?
        val message: String?
        try {
            val body = json.parseToJsonElement(raw).jsonObject
            val all = body["__all__"]?.jsonObject
            code = all?.get("code")?.jsonPrimitive?.content
            message = all?.get("message")?.jsonPrimitive?.content
        } catch (_: Exception) {
            throw DodeSoftException.ApiException(
                statusCode = response.status.value,
                code = null,
                errorMessage = raw,
                raw = raw,
            )
        }
        throw DodeSoftException.ApiException(
            statusCode = response.status.value,
            code = code,
            errorMessage = message,
            raw = raw,
        )
    }
}
