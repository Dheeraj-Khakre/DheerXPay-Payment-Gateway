# Multi-Tenant Payment Gateway

A payment gateway that lets merchants create payment orders, collect payments through multiple methods, issue refunds, receive payment webhooks, and get settlements and analytics.

> **Project status:** This README documents the requested requirements and a proposed high-level architecture. It does not claim that the services or performance targets have already been implemented or verified.

## Contents

- [1. Project overview](#1-project-overview)
- [2. High-level architecture](#2-high-level-architecture)
- [3. Functional requirements](#3-functional-requirements)
  - [Merchant management](#merchant-management)
  - [Order lifecycle](#order-lifecycle)
  - [Payment lifecycle](#payment-lifecycle)
  - [Refund lifecycle](#refund-lifecycle)
  - [Webhook delivery](#webhook-delivery)
  - [Settlement](#settlement)
  - [Card vault](#card-vault)
  - [Analytics](#analytics)
  - [Multi-tenant security](#multi-tenant-security)
- [4. Non-functional requirements](#4-non-functional-requirements)
- [5. Main end-to-end flows](#5-main-end-to-end-flows)
- [6. Reliability and security notes](#6-reliability-and-security-notes)
- [7. Decisions to finalize](#7-decisions-to-finalize)

## 1. Project overview

The platform provides a common payment layer for multiple merchants. Each merchant has its own account, API credentials, data boundaries, webhook configuration, and analytics view.

### Main capabilities

- Merchant onboarding and API key management.
- Order creation, lookup, listing, expiry, and duplicate-request protection.
- Payment collection through cards, UPI intent, net banking, and wallets.
- Refund processing, including asynchronous execution.
- Signed webhooks with retries, dead-letter handling, and replay.
- T+1 settlement calculation and bank-transfer processing.
- Secure card tokenization and merchant-level analytics.

## 2. High-level architecture

The picture below shows the main actors, API/security layer, business services, external payment systems, and shared infrastructure. It is a logical view: the exact deployment topology and technology choices can be decided during implementation.

![Payment gateway high-level architecture](docs/architecture.png)

### Component responsibilities

| Component | Responsibility |
|---|---|
| Merchant application | Calls server-to-server APIs to create orders and initiate payments. |
| Merchant dashboard | Allows a merchant user to sign in and view payment and revenue information. |
| API gateway / REST API | Receives requests, validates them, routes them to the correct service, and applies common API controls. |
| Merchant service | Handles onboarding, KYC status, login-related account data, and API key creation. |
| Order service | Creates and retrieves orders, applies filters and pagination, and expires unpaid orders. |
| Payment service | Starts payments, integrates with an acquirer, and manages payment status transitions. |
| Refund service | Validates refund requests and processes refunds asynchronously when required. |
| Card vault | Converts card details into tokens and protects stored PAN data. |
| Webhook delivery service | Signs event payloads, sends merchant notifications, retries failures, and supports replay. |
| Settlement service | Calculates merchant settlement amounts and coordinates the bank-transfer step. |
| Analytics service | Aggregates payment and revenue data for dashboards and historical reports. |
| Database / durable storage | Persists merchant, order, payment, refund, card-token, settlement, and audit records. |
| Message broker, retry queue, and DLQ | Carries asynchronous work, schedules retries, and stores events that exhaust their retry attempts. |
| Scheduler | Triggers order expiry, asynchronous refund work, and nightly settlement jobs. |
| Observability | Collects logs, metrics, traces, and dead-letter information for operations and support. |

## 3. Functional requirements

### Merchant management

- **Merchant onboarding, account creation, and KYC:** A business can register, create its merchant account, and submit the information needed for verification (Know Your Customer).
- **Email and password login:** Merchant users can sign in with their registered email address and password.
- **API key generation with a show-once secret:** A merchant can generate credentials for server-to-server API calls. The secret is displayed only when it is created, so the merchant must copy and store it securely at that time.

### Order lifecycle

- **Create an order with amount, notes, and expiry:** The merchant creates a payment request with the amount to collect, optional description/notes, and an expiry time.
- **List orders with pagination and filtering:** The merchant can browse orders in smaller pages and narrow the results using supported filters, rather than loading every order at once.
- **Get an order by ID:** The merchant can retrieve the details and current status of one specific order.
- **Automatic expiry after 15 minutes:** An order that has not been paid expires 15 minutes after creation, according to the stated default rule.
- **Idempotent order creation using `X-Idempotency-Key`:** The client sends a unique key with the create-order request. Repeating the same request with the same key should return the original result instead of creating a duplicate order.

### Payment lifecycle

- **Initiate payment for an order with method details:** The merchant starts a payment against an existing order and supplies the details needed for the selected payment method.
- **Support cards, UPI intent, net banking, and wallets:** Customers can choose among the listed payment methods.
- **Mock payment acquirer:** A simulated acquirer is available for development and testing, so payment success, failure, delay, and other scenarios can be exercised without a live payment provider.
- **Payment state machine:** A payment moves through explicitly controlled statuses. Each transition should be validated so that invalid changes—such as marking a failed payment as successful without a valid event—are rejected. The exact status names and transition rules must be finalized.

### Refund lifecycle

- **Refund only after payment is complete:** A refund can be requested only for a successfully completed payment.
- **Refund state machine:** Refunds have their own statuses and allowed transitions, separate from the original payment status.
- **Asynchronous refund processing through a scheduler:** Refund work can run in the background. A scheduled worker picks up eligible refunds, processes them, and updates their status without making the API request wait for the full operation.

### Webhook delivery

- **Per-merchant webhook URL configuration:** Each merchant can configure the endpoint where payment and other supported event notifications should be sent.
- **HMAC-SHA256 payload signing:** Each webhook includes a cryptographic signature calculated from the payload and a merchant-specific secret. The merchant can verify that the message came from the gateway and was not modified in transit.
- **Seven-attempt retry system:** When delivery fails, the system tries again, up to the configured limit of seven attempts. The retry intervals and which HTTP responses count as failures should be defined in implementation settings.
- **Dead-letter queue after seven retries:** An event that still cannot be delivered after the retry limit is moved to a dead-letter queue (DLQ) for investigation and recovery.
- **Replay API for dead-lettered webhooks:** An authorized operator or merchant workflow can request another delivery attempt for an event stored in the DLQ. Replayed events should retain their event ID so receivers can deduplicate them.

### Settlement

- **Nightly batch settlement:** A scheduled job groups eligible captured payments and prepares settlement records for the nightly processing cycle.
- **Fee, GST, and total settlement calculation:** The system calculates applicable fees and GST, then derives the net amount payable to each merchant. The exact fee rules, tax basis, and rounding method must be configured.
- **Settlement record with a full audit trail:** Every settlement should retain its inputs, calculated amounts, status changes, timestamps, and processing references so the result can be traced later.
- **Mock bank transfer with chaos testing:** A simulated bank-transfer integration supports testing normal transfers and controlled failures, delays, or timeouts. This helps verify retries and recovery behavior.

### Card vault

- **Tokenization API:** The merchant can exchange card details for a token. Future supported payment operations use the token instead of repeatedly handling the original card number.
- **AES-256 encryption of PAN:** The Primary Account Number (PAN, or card number) is encrypted when stored. Encryption keys should be protected and managed separately from the encrypted data.
- **Charge using a token:** The payment flow can charge a previously created card token, subject to the token's validity and the merchant's permissions.
- **`@MaskedCard` annotation and Logback filter for log safety:** Sensitive card values should be masked before they reach application logs. The annotation identifies fields or values to mask, while the logging filter provides an additional safeguard against accidental PAN exposure.

### Analytics

- **Real-time dashboard for today's and the last seven days' revenue:** The dashboard displays recent revenue totals with updates as payment events are processed.
- **Per-merchant analytics filter:** Users can view metrics for a selected merchant, while access controls prevent one merchant from seeing another merchant's data.
- **Historical reports:** The platform provides access to older payment and revenue summaries for analysis and reconciliation.

### Multi-tenant security

- **API key authentication using the Basic authentication header for server-to-server endpoints:** Merchant backend systems authenticate API requests using their issued API credentials in the expected Basic authentication format. API traffic must use TLS, and credentials must never be written to logs.
- **JWT authentication for dashboard endpoints:** Dashboard users receive a JSON Web Token after successful login and send it with later requests to access protected dashboard APIs.
- **Per-merchant rate limiting:** Request limits are applied separately to each merchant so that one merchant's traffic cannot consume all shared API capacity.

## 4. Non-functional requirements

These are system-wide quality and performance targets, not individual features.

| Requirement | Target | Simple explanation |
|---|---:|---|
| Throughput | 10,000 TPS | The system should be designed to handle up to 10,000 transactions per second at the agreed workload and measurement point. |
| Latency | p99 below 1 second | 99% of measured requests should complete in less than one second. The measured API boundary and excluded external-provider wait time should be specified. |
| Availability | 99.99% | The service should be operational for 99.99% of the agreed measurement period, excluding only formally defined exclusions. |
| Durability | Zero payment loss, including during failures | Once a payment operation is accepted as durable, failures must not silently erase it. Persistent records and recovery mechanisms are required. |
| Idempotency | 24-hour window for every write API | Repeating a write request with the same idempotency key during the 24-hour window must not perform the operation twice. |
| Webhook SLA | 99% delivered within 30 seconds; 100% within 24 hours | Notifications should be delivered quickly in normal conditions, with a recovery path for delayed deliveries. The DLQ and replay process must be included in how this target is measured. |
| Settlement | T+1, within 24 hours of capture | Eligible captured funds should be included in settlement by the next-day target, within 24 hours of capture as specified. |
| Security | PCI DSS compliance and HMAC-signed webhooks | Card-data handling must meet the required payment-card security controls, and webhook payloads must be verifiable using their signatures. |
| Observability | DLQ events and payment analytics on dashboard | Operators should be able to inspect failed asynchronous events, and business users should be able to monitor payment performance and revenue. |

**Terminology:** `TPS` means transactions per second. `p99` is the 99th percentile, so only 1% of measured requests may be slower than the stated limit. `T+1` means the next-day settlement cycle. `DLQ` means dead-letter queue. `PAN` means card number.

## 5. Main end-to-end flows

### A. Merchant creates an order and collects a payment

1. The merchant signs in or authenticates its server-to-server API request.
2. The merchant creates an order with an amount and optional notes.
3. The order service stores the order and returns its ID and expiry information.
4. The merchant initiates a payment using a supported payment method.
5. The payment service communicates with the configured acquirer (mock acquirer in test mode).
6. The payment state is updated and stored durably.
7. A payment event is placed onto asynchronous messaging infrastructure for webhook delivery and analytics.
8. The webhook service signs the event and sends it to the merchant's configured URL.

### B. A webhook delivery fails

1. The delivery worker sends the signed webhook to the merchant endpoint.
2. If delivery fails, the worker retries according to the configured retry policy, up to seven attempts.
3. If all attempts fail, the event is moved to the DLQ.
4. The failure is visible to operations, and an authorized replay operation can trigger another attempt.

### C. A merchant requests a refund

1. The merchant requests a refund for a successfully completed payment.
2. The refund service validates the payment, amount, and current refund state.
3. The refund is recorded and queued for background processing.
4. The scheduled worker processes the refund and updates its state.
5. The result can be communicated through the supported event/webhook flow.

### D. Nightly settlement

1. The scheduler starts the nightly settlement batch.
2. The settlement service selects eligible captured payments.
3. It calculates fees, GST, and the net settlement amount.
4. It stores settlement details and audit records.
5. The bank-transfer adapter (mock in test mode) processes the transfer and records its result.

## 6. Reliability and security notes

The following are implementation practices that help satisfy the requirements; they are design recommendations rather than additional confirmed features:

- **Persist before acknowledging:** Store accepted payment operations durably before returning a success response to the caller.
- **Enforce idempotency in storage:** Use a uniqueness constraint or equivalent atomic mechanism scoped to the merchant and idempotency key. Store the original response so a repeat request can receive the same result.
- **Publish events reliably:** Consider a transactional outbox or an equivalent mechanism so a committed payment update cannot be separated from the event that must be delivered.
- **Make consumers safe to retry:** Webhook, refund, settlement, and analytics workers should tolerate duplicate messages and safely resume after a crash.
- **Control payment state transitions:** Validate each transition and preserve a history of important status changes.
- **Isolate tenant data:** Derive tenant identity from authenticated credentials, enforce tenant scoping in every data access path, and test for cross-merchant access.
- **Protect secrets and card data:** Avoid logging credentials or PAN, use TLS for network calls, protect encryption/signing keys, and restrict access to sensitive records.
- **Measure each target:** Define the load-test workload for 10,000 TPS, the exact latency boundary for p99, and the availability measurement window before claiming the targets are met.

## 7. Decisions to finalize

A few details need explicit decisions before implementation and testing:

1. **Payment and refund statuses:** Define the full status lists, allowed transitions, terminal states, and how late or duplicate acquirer callbacks are handled.
2. **Idempotency scope:** Confirm key uniqueness scope (for example, per merchant and API operation), behavior when the same key is reused with a different request body, and retention at the end of the 24-hour window.
3. **Webhook retry schedule:** Define retry delays, timeout, retryable HTTP/network errors, and whether the initial delivery is included in the seven attempts.
4. **Webhook 24-hour guarantee:** Clarify whether the 100% target includes merchant endpoints that remain unavailable, and how DLQ replay and operational escalation count toward the SLA.
5. **Settlement calendar:** Define timezone, cutoff time, weekends/holidays, eligible payment statuses, refund/chargeback adjustments, fee rules, GST basis, and rounding.
6. **Order expiry behavior:** Define what happens if a payment is processing when its 15-minute order expiry is reached, and how late acquirer success callbacks are reconciled.
7. **PCI DSS scope:** Confirm which components handle raw card data and which controls, assessments, and hosting boundaries are required for the intended compliance scope.
8. **Performance boundaries:** Define whether external acquirer and bank response times are included in the p99 API latency target and specify the expected traffic mix for the 10,000 TPS test.

---

## Suggested implementation order

1. Merchant authentication, tenant isolation, and API key management.
2. Order APIs, persistence, expiry, and idempotency.
3. Payment state machine and mock acquirer.
4. Durable event publishing and signed webhook delivery with retries/DLQ/replay.
5. Refund state machine and asynchronous processing.
6. Card tokenization and sensitive-data protections.
7. Settlement batch, fee/tax calculation, and mock bank transfer.
8. Analytics, dashboards, observability, load testing, and resilience testing.
