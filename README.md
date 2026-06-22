# dodesoft-kotlin

The official Kotlin SDK for the [DodeSoft](https://dodesoft.com) payment gateway.

[![Maven Central](https://img.shields.io/maven-central/v/com.dodesoft/dodesoft-kotlin)](https://central.sonatype.com/artifact/com.dodesoft/dodesoft-kotlin)
[![Kotlin](https://img.shields.io/badge/kotlin-2.2%2B-blue)](https://kotlinlang.org)
[![JVM](https://img.shields.io/badge/jvm-21%2B-blue)](https://adoptium.net)

## Requirements

- Kotlin 2.2 or later
- JVM 21 or later

## Installation

**Gradle (Kotlin DSL)**

```kotlin
dependencies {
    implementation("com.dodesoft:dodesoft-kotlin:0.1.0")
}
```

**Gradle (Groovy DSL)**

```groovy
dependencies {
    implementation 'com.dodesoft:dodesoft-kotlin:0.1.0'
}
```

**Maven**

```xml
<dependency>
    <groupId>com.dodesoft</groupId>
    <artifactId>dodesoft-kotlin</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Quick start

All API calls are `suspend` functions — call them from a coroutine scope or `runBlocking` in tests.

```kotlin
import com.dodesoft.sdk.DodeSoftClient
import com.dodesoft.sdk.DodeSoftConfig
import com.dodesoft.sdk.models.*
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val client = DodeSoftClient(DodeSoftConfig(
        apiKey = System.getenv("DODESOFT_API_KEY")
    ))

    val purchase = client.purchases.create(CreatePurchaseRequest(
        brandId = "your-brand-id",
        purchase = PurchaseDetails(
            currency = "EUR",
            total = 1000L,             // €10.00
            products = listOf(
                Product(name = "T-shirt", price = 1000)
            )
        ),
        client = ClientDetails(email = "customer@example.com"),
        successRedirect = "https://yourstore.com/success",
        failureRedirect = "https://yourstore.com/failure"
    ))

    println("Checkout URL: ${purchase.checkoutUrl}")
    client.close()
}
```

Use the client as an `AutoCloseable` to ensure the underlying HTTP connection pool is released:

```kotlin
DodeSoftClient(config).use { client ->
    val purchase = client.purchases.get("purchase-id")
}
```

## Authentication

Pass your **secret key** via `DodeSoftConfig` or the `DODESOFT_API_KEY` environment variable. Never embed it in client-side or Android code.

```kotlin
// Explicit key
val config = DodeSoftConfig(apiKey = "sk_live_...")

// From environment (recommended for production)
val config = DodeSoftConfig(apiKey = System.getenv("DODESOFT_API_KEY"))
```

## Configuration

```kotlin
val config = DodeSoftConfig(
    apiKey  = System.getenv("DODESOFT_API_KEY"),
    baseUrl = "https://gate.dodesoft.com/api/v1"  // default
)
```

## Resources

### Purchases

```kotlin
// Create a hosted-checkout purchase
val purchase = client.purchases.create(CreatePurchaseRequest(...))

// Retrieve
val purchase = client.purchases.get("purchase-id")

// Cancel
val purchase = client.purchases.cancel("purchase-id")

// Capture a pre-authorised amount (partial or full)
val purchase = client.purchases.capture("purchase-id", CaptureRequest(amount = 500L))

// Release a pre-authorisation
val purchase = client.purchases.release("purchase-id")

// Charge via recurring token
val purchase = client.purchases.charge("purchase-id", ChargeRequest(recurringToken = "token-id"))

// Refund (full or partial — omit amount for full refund)
val payment = client.purchases.refund("purchase-id", RefundRequest(amount = 500L))

// Delete recurring token
val purchase = client.purchases.deleteRecurringToken("purchase-id")

// Mark as paid (admin use)
val purchase = client.purchases.markAsPaid("purchase-id", MarkAsPaidRequest(paidOn = Instant.now().epochSecond))

// Resend invoice email
val purchase = client.purchases.resendInvoice("purchase-id")
```

#### Purchase status helpers

```kotlin
if (purchase.status.isFinal) {
    // paid, cancelled, expired, refunded, or released
}
if (purchase.status.isPending) {
    // one of the pending_* states
}
```

### Payment Methods

```kotlin
val methods = client.paymentMethods.list(
    brandId  = "your-brand-id",
    currency = "EUR",
    country  = "DE"
)
```

### Payouts

```kotlin
// Available payout methods
val methods = client.payouts.listMethods()

// Create a payout
val payout = client.payouts.create(CreatePayoutRequest(
    brandId = "your-brand-id",
    payment = PaymentDetails(amount = 5000L, currency = "EUR"),
    client  = ClientDetails(email = "recipient@example.com")
))

// Retrieve
val payout = client.payouts.get("payout-id")
```

### Clients

```kotlin
// Create a stored client profile
val record = client.clients.create(ClientDetails(
    email    = "customer@example.com",
    fullName = "Jane Doe",
    country  = "DE"
))

// CRUD
val records = client.clients.list()
val record  = client.clients.get("client-id")
val record  = client.clients.update("client-id", details)
val record  = client.clients.patch("client-id", details)
client.clients.delete("client-id")

// Recurring tokens
val tokens = client.clients.listRecurringTokens("client-id")
val token  = client.clients.getRecurringToken("client-id", "token-id")
client.clients.deleteRecurringToken("client-id", "token-id")
```

### Webhooks

```kotlin
// Create
val webhook = client.webhooks.create(CreateWebhookRequest(
    title    = "Order events",
    callback = "https://yoursite.com/webhooks/dodesoft",
    events   = listOf(WebhookEvent.PURCHASE_PAID, WebhookEvent.PAYMENT_REFUNDED)
))

// CRUD
val webhooks  = client.webhooks.list()
val webhook   = client.webhooks.get("webhook-id")
val webhook   = client.webhooks.update("webhook-id", req)
val webhook   = client.webhooks.patch("webhook-id", patchReq)
client.webhooks.delete("webhook-id")

// Delivery history
val deliveries = client.webhooks.deliveries("webhook-id", "purchase")
```

### Billing

```kotlin
// Send one-off invoices
val result = client.billing.createInvoices(buildJsonObject { ... })

// Templates
val template  = client.billing.createTemplate(buildJsonObject { ... })
val templates = client.billing.listTemplates()
val template  = client.billing.getTemplate("template-id")
val template  = client.billing.updateTemplate("template-id", buildJsonObject { ... })
client.billing.deleteTemplate("template-id")

// Subscribers
val result = client.billing.addSubscriber("template-id", buildJsonObject { ... })
val subs   = client.billing.listSubscribers("template-id")
val sub    = client.billing.getSubscriber("template-id", "client-id")
val sub    = client.billing.updateSubscriber("template-id", "client-id", buildJsonObject { ... })
```

### Account

```kotlin
// Flat account balance
val balance = client.account.balance()

// Per-currency balance totals, optionally since a Unix timestamp
val balance = client.account.balanceByCurrency(since = 1700000000L)
val balance = client.account.balanceByCurrency()          // all time

// Turnover
val turnover = client.account.turnover()

// Company statements
val stmt  = client.account.createStatement(buildJsonObject { ... })
val stmts = client.account.listStatements()
val stmt  = client.account.getStatement("statement-id")
val stmt  = client.account.cancelStatement("statement-id")
```

## Webhook verification

DodeSoft signs every webhook delivery with an RSA PKCS#1 v1.5 / SHA-256 signature in the `X-Signature` header.

```kotlin
import com.dodesoft.sdk.signature.SignatureVerifier
import com.dodesoft.sdk.models.WebhookEvent

// In your HTTP handler (Spring, Ktor, etc.)
suspend fun handleWebhook(body: ByteArray, signatureHeader: String) {
    // Fetch the public key once and cache it
    val publicKeyPem = client.webhooks.getPublicKey().publicKey

    val payload = client.webhooks.parseVerified(body, signatureHeader, publicKeyPem)
    // parseVerified throws DodeSoftException.SignatureException on failure

    when (payload.event) {
        WebhookEvent.PURCHASE_PAID      -> fulfilOrder(payload)
        WebhookEvent.PAYMENT_REFUNDED   -> processRefund(payload)
        else                            -> Unit
    }
}
```

Verify without parsing:

```kotlin
SignatureVerifier.verifyOrThrow(body, signatureHeader, publicKeyPem)
// or
val ok = SignatureVerifier.verify(body, signatureHeader, publicKeyPem)
```

## Error handling

The SDK uses a sealed `DodeSoftException` hierarchy:

```kotlin
import com.dodesoft.sdk.DodeSoftException

try {
    val purchase = client.purchases.create(req)
} catch (e: DodeSoftException.ApiException) {
    println("API error ${e.statusCode} — ${e.code}: ${e.errorMessage}")
} catch (e: DodeSoftException.NetworkException) {
    println("Network error: ${e.message}")
} catch (e: DodeSoftException.SignatureException) {
    println("Invalid webhook signature: ${e.message}")
}
```

| Exception | When thrown |
|---|---|
| `DodeSoftException.ApiException` | Non-2xx HTTP response from the API |
| `DodeSoftException.NetworkException` | I/O failure before a response was received |
| `DodeSoftException.SerializationException` | Response body could not be deserialised |
| `DodeSoftException.SignatureException` | Webhook signature failed verification |

## Money amounts

All monetary values are `Long` integers in the **minor currency unit** (e.g. cents for EUR/USD). `1000L` = €10.00. There are no `Double` amounts in the API.

## Examples

Runnable examples are in the [`examples/`](examples/) directory.

```bash
DODESOFT_API_KEY=sk_live_... ./gradlew run
```

## Development

```bash
./gradlew test
./gradlew build
```

## License

MIT
