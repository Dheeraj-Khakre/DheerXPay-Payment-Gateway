# Payment Gateway -- Database ER Diagram

## 1. Project overview

This database design represents a **multi-merchant payment gateway**.
Multiple merchants can use the platform to create orders, accept
payments, tokenize cards, issue refunds, receive webhook notifications,
and get settlements.

The design also supports operational and reliability requirements such
as:

-   **Idempotency** -- helps prevent duplicate orders or payments when a
    client retries a request.
-   **Card vaulting and tokenization** -- stores card data in encrypted
    form and gives the application a token to use instead of exposing
    the card number.
-   **Webhook retries and dead-letter handling** -- retries failed event
    deliveries and stores events that cannot be delivered.
-   **Payment audit history** -- records every payment status
    transition.
-   **Settlement reconciliation** -- connects payments to merchant
    settlement batches and stores the financial totals.

> **Important:** This README explains the entities and fields visible in
> the supplied ER diagram. Where a relationship is implied by a foreign
> key but its connector is not clearly drawn, it is identified as an
> FK-based relationship.

------------------------------------------------------------------------

## 2. How to read the diagram

  -----------------------------------------------------------------------
  Term                                Meaning
  ----------------------------------- -----------------------------------
  Entity / table                      A database table that stores one
                                      type of information.

  Attribute / column                  A field inside a table.

  PK (Primary Key)                    Uniquely identifies one row in a
                                      table.

  FK (Foreign Key)                    Refers to a row in another table
                                      and maintains the relationship.

  UK (Unique Key)                     Prevents duplicate values in a
                                      column or constraint.

  UUID                                A unique identifier suitable for
                                      distributed systems.

  `long`                              A large integer; monetary values
                                      here are stored in paise.

  `jsonb`                             PostgreSQL JSON data type, useful
                                      for structured and flexible data.

  `bytes`                             Binary data, used here for
                                      encrypted card material.

  `datetime`                          Date and time at which an event
                                      occurred.
  -----------------------------------------------------------------------

### Monetary values

The diagram uses names ending in `_paise`, which means amounts are
stored as integer paise rather than floating-point rupees.

For example:

-   `₹100.00` = `10000` paise
-   `₹25.50` = `2550` paise

This avoids floating-point rounding errors in financial calculations.
Convert paise to rupees only when displaying the amount.

------------------------------------------------------------------------

## 3. Entities and attributes

Each attribute is explained in one simple line. `PK`, `FK`, and `UK`
indicate the constraints shown in the diagram.

### 3.1 MERCHANT

**Purpose:** Stores the business account that uses the payment gateway.
Most payment data is associated with a merchant so that one merchant's
data can be separated from another's.

  ----------------------------------------------------------------------------------
  Attribute                          Type / Key              Simple explanation
  ---------------------------------- ----------------------- -----------------------
  `id`                               UUID, PK                Unique ID of the
                                                             merchant.

  `name`                             string                  Contact person's or
                                                             merchant's display
                                                             name.

  `business_name`                    string                  Registered or operating
                                                             name of the business.

  `email`                            string                  Merchant's contact
                                                             email address.

  `status`                           string                  Current merchant
                                                             account status, such as
                                                             active or suspended.

  `gst_id`                           string                  GST registration
                                                             identifier of the
                                                             business.

  `pan`                              string                  PAN identifier
                                                             associated with the
                                                             business.

  `settlement_bank_account`          string                  Bank account used to
                                                             transfer settlement
                                                             money.

  `settlement_ifsc`                  string                  IFSC code identifying
                                                             the settlement bank
                                                             branch.

  `settlement_account_holder_name`   string                  Name of the bank
                                                             account holder.

  `created_at`                       datetime                Date and time when the
                                                             merchant was created.

  `updated_at`                       datetime                Date and time when the
                                                             merchant record was
                                                             last updated.

  `created_by`                       string                  User or process that
                                                             created the merchant
                                                             record.

  `updated_by`                       string                  User or process that
                                                             last updated the
                                                             merchant record.
  ----------------------------------------------------------------------------------

**Interview explanation:** The `MERCHANT` table is the tenant or
business root of the platform. Merchant-owned records carry
`merchant_id`, which helps enforce tenant-level data isolation.

### 3.2 API_KEY

**Purpose:** Stores API credentials that a merchant uses to authenticate
requests to the payment gateway.

  ---------------------------------------------------------------------------
  Attribute                   Type / Key              Simple explanation
  --------------------------- ----------------------- -----------------------
  `id`                        UUID, PK                Unique ID of this API
                                                      key record.

  `merchant_id`               UUID, FK                Merchant that owns this
                                                      API key.

  `key_id`                    string, UK              Public or lookup
                                                      identifier used to find
                                                      the API credential.

  `key_secret_hash`           string                  Hash of the API secret;
                                                      the raw secret should
                                                      not be stored.

  `webhook_secret_hash`       string                  Hash of the secret used
                                                      to verify webhook
                                                      signatures.

  `environment`               string                  Environment for the
                                                      key, such as test or
                                                      live.

  `enabled`                   boolean                 Indicates whether the
                                                      key can currently be
                                                      used.

  `last_used_at`              datetime                Most recent time the
                                                      API key was used.

  `created_at`                datetime                Date and time when the
                                                      key was created.

  `rotated_at`                datetime                Date and time when the
                                                      key was last rotated.

  `grace_period_expires_at`   datetime                Time until which an old
                                                      key may remain valid
                                                      during rotation.
  ---------------------------------------------------------------------------

**Interview explanation:** API keys authenticate merchant API calls. The
platform stores a key identifier and secret hash rather than storing the
raw secret. Key rotation and environment separation improve security and
operations.

### 3.3 APP_USER

**Purpose:** Stores dashboard users who belong to a merchant.

  -----------------------------------------------------------------------
  Attribute               Type / Key              Simple explanation
  ----------------------- ----------------------- -----------------------
  `id`                    UUID, PK                Unique ID of the
                                                  dashboard user.

  `merchant_id`           UUID, FK                Merchant account to
                                                  which the user belongs.

  `email`                 string, UK              User's unique login
                                                  email.

  `password_hash`         string                  Secure hash of the
                                                  user's password.

  `role`                  string                  User's permission role,
                                                  such as admin or
                                                  operator.

  `createdAt`             datetime                Date and time when the
                                                  user was created.

  `updatedAt`             datetime                Date and time when the
                                                  user was last updated.
  -----------------------------------------------------------------------

**Interview explanation:** `APP_USER` is for people accessing the
merchant dashboard. It is different from `CUSTOMER`, which represents a
buyer making a payment.

### 3.4 MERCHANT_WEBHOOK_CONFIG

**Purpose:** Stores the webhook destination and event preferences
configured by a merchant.

  -----------------------------------------------------------------------
  Attribute               Type / Key              Simple explanation
  ----------------------- ----------------------- -----------------------
  `id`                    UUID, PK                Unique ID of the
                                                  webhook configuration.

  `merchant_id`           UUID, FK                Merchant that owns this
                                                  configuration.

  `target_url`            string                  Endpoint where webhook
                                                  events should be sent.

  `event_type_filter`     string                  Event types the
                                                  merchant wants to
                                                  receive.

  `enabled`               boolean                 Indicates whether
                                                  webhook delivery is
                                                  enabled.

  `webhook_secret`        string                  Secret used to sign or
                                                  verify webhook
                                                  messages.

  `created_at`            datetime                Date and time when the
                                                  configuration was
                                                  created.
  -----------------------------------------------------------------------

**Interview explanation:** This table separates merchant webhook
preferences from individual webhook delivery attempts. In production,
protect the webhook secret using encryption or a suitable
secret-management system; do not expose it in logs.

### 3.5 CUSTOMER

**Purpose:** Stores a buyer's details under a merchant account.

  -----------------------------------------------------------------------
  Attribute               Type / Key              Simple explanation
  ----------------------- ----------------------- -----------------------
  `id`                    UUID, PK                Unique ID of the
                                                  customer.

  `merchant_id`           UUID, FK                Merchant with which the
                                                  customer is associated.

  `name`                  string                  Customer's name.

  `email`                 string                  Customer's email
                                                  address.

  `phone`                 string                  Customer's phone
                                                  number.

  `gst_id`                string                  Customer GST
                                                  identifier, if
                                                  applicable.

  `created_at`            datetime                Date and time when the
                                                  customer was created.

  `updated_at`            datetime                Date and time when the
                                                  customer was last
                                                  updated.
  -----------------------------------------------------------------------

**Interview explanation:** A customer is scoped to a merchant. The same
real-world person could potentially have separate customer records for
different merchants, depending on the platform's tenant model.

### 3.6 ORDER_RECORD

**Purpose:** Stores the merchant's request to collect a specific amount
from a customer. It represents the order before or while payment
processing happens.

  -----------------------------------------------------------------------
  Attribute               Type / Key              Simple explanation
  ----------------------- ----------------------- -----------------------
  `id`                    UUID, PK                Unique ID of the order.

  `merchant_id`           UUID, FK                Merchant that created
                                                  the order.

  `idempotency_key`       string                  Client-provided key
                                                  used to recognize a
                                                  retry of the same
                                                  request.

  `amount_paise`          long                    Order amount stored in
                                                  paise.

  `status`                string                  Current order status.

  `attempts`              int                     Number of payment or
                                                  processing attempts
                                                  recorded for the order.

  `notes`                 jsonb                   Additional order
                                                  information in JSON
                                                  format.

  `expires_at`            datetime                Time after which the
                                                  order is no longer
                                                  valid.

  `created_by`            string                  User or process that
                                                  created the order.

  `updated_by`            string                  User or process that
                                                  last updated the order.
  -----------------------------------------------------------------------

**Interview explanation:** An order is the business request to collect
money. A payment is an attempt to fulfil that request. Keeping them
separate allows an order to have multiple payment attempts.

### 3.7 PAYMENT

**Purpose:** Stores each payment attempt and its processing result.

  -------------------------------------------------------------------------
  Attribute                 Type / Key              Simple explanation
  ------------------------- ----------------------- -----------------------
  `id`                      UUID, PK                Unique ID of the
                                                    payment.

  `order_id`                UUID, FK                Order for which this
                                                    payment was created.

  `merchant_id`             UUID, FK                Merchant that owns the
                                                    payment.

  `idempotency_key`         string                  Key used to identify a
                                                    retry of the payment
                                                    request.

  `amount_paise`            long                    Payment amount stored
                                                    in paise.

  `status`                  string                  Current payment state,
                                                    such as created,
                                                    pending, captured, or
                                                    failed.

  `method`                  string                  Payment method, such as
                                                    card or another
                                                    supported method.

  `method_details`          jsonb                   Method-specific
                                                    information needed for
                                                    processing.

  `bank_reference`          string                  Reference returned by
                                                    the bank or payment
                                                    processor.

  `error_code`              string                  Machine-readable code
                                                    explaining a payment
                                                    failure.

  `error_description`       string                  Human-readable
                                                    explanation of a
                                                    payment failure.

  `refunded_amount_paise`   long                    Total amount refunded
                                                    against this payment,
                                                    in paise.

  `captured_at`             datetime                Time when the payment
                                                    was captured.

  `settled_at`              datetime                Time when the payment
                                                    was settled.

  `created_at`              datetime                Date and time when the
                                                    payment record was
                                                    created.

  `updated_at`              datetime                Date and time when the
                                                    payment record was last
                                                    updated.
  -------------------------------------------------------------------------

**Interview explanation:** `PAYMENT` is the central transaction table.
It stores the result of each attempt, while the order stores the
original collection request. Status changes are separately recorded in
`PAYMENT_TRANSITION_LOG`.

### 3.8 VAULT_CARD

**Purpose:** Stores encrypted card data in a controlled card vault.

  -----------------------------------------------------------------------
  Attribute               Type / Key              Simple explanation
  ----------------------- ----------------------- -----------------------
  `id`                    UUID, PK                Unique ID of the
                                                  vaulted card record.

  `encrypted_pan`         bytes                   Encrypted card number;
                                                  the raw PAN should not
                                                  be stored here.

  `encrypted_dek`         bytes                   Encrypted Data
                                                  Encryption Key used to
                                                  protect the card data.

  `last_four`             string                  Last four digits used
                                                  for safe card display.

  `brand`                 string                  Card network or brand,
                                                  such as Visa or
                                                  Mastercard.

  `bin`                   string                  Bank Identification
                                                  Number or issuer
                                                  identification digits.

  `expiry_month`          int                     Card expiry month.

  `expiry_year`           int                     Card expiry year.

  `created_at`            datetime                Date and time when the
                                                  card was vaulted.

  `updated_at`            datetime                Date and time when the
                                                  vault record was last
                                                  updated.
  -----------------------------------------------------------------------

**Interview explanation:** The vault isolates sensitive card data from
the rest of the payment application. Encryption keys should be managed
securely, with strict access controls and appropriate PCI DSS controls.

### 3.9 CARD_TOKEN

**Purpose:** Maps a reusable token to a vaulted card and the
customer/merchant allowed to use it.

  -----------------------------------------------------------------------
  Attribute               Type / Key              Simple explanation
  ----------------------- ----------------------- -----------------------
  `id`                    UUID, PK                Unique ID of the
                                                  card-token record.

  `token`                 string, UK              Unique token presented
                                                  by the application
                                                  instead of the card
                                                  number.

  `vault_card_id`         UUID, FK                Reference to the
                                                  encrypted card in
                                                  `VAULT_CARD`.

  `customer_id`           UUID, FK                Customer to whom the
                                                  token is associated.

  `merchant_id`           UUID, FK                Merchant allowed to use
                                                  the token.

  `created_at`            datetime                Date and time when the
                                                  token was created.

  `updated_at`            datetime                Date and time when the
                                                  token was last updated.
  -----------------------------------------------------------------------

**Interview explanation:** Tokenization replaces direct use of card
details with a token. The application can use the token for permitted
future transactions without repeatedly handling the card number.

### 3.10 WEBHOOK_EVENT

**Purpose:** Stores an event that the gateway needs to deliver to a
merchant's webhook endpoint.

  -----------------------------------------------------------------------
  Attribute               Type / Key              Simple explanation
  ----------------------- ----------------------- -----------------------
  `id`                    UUID, PK                Unique ID of the
                                                  webhook event.

  `merchant_id`           UUID, FK                Merchant that should
                                                  receive the event.

  `event_type`            string                  Type of event, such as
                                                  payment captured or
                                                  payment failed.

  `payload`               jsonb                   Event data sent to the
                                                  merchant.

  `target_url`            string                  Destination URL for
                                                  this delivery.

  `status`                string                  Current delivery
                                                  status.

  `attempts`              int                     Number of delivery
                                                  attempts.

  `last_response_code`    int                     HTTP response code from
                                                  the most recent
                                                  attempt.

  `next_retry_at`         datetime                Scheduled time for the
                                                  next retry.

  `last_retry_at`         datetime                Time of the most recent
                                                  retry.

  `created_at`            datetime                Date and time when the
                                                  event was created.

  `delivered_at`          datetime                Time when delivery
                                                  succeeded.
  -----------------------------------------------------------------------

**Interview explanation:** Webhook events are stored so delivery can
happen asynchronously and be retried if the merchant's server is
temporarily unavailable. A successful payment should not depend on the
merchant endpoint being online at that exact moment.

### 3.11 DLQ_EVENT

**Purpose:** Stores webhook events that exhausted their delivery retries
or otherwise failed permanently.

  -----------------------------------------------------------------------
  Attribute               Type / Key              Simple explanation
  ----------------------- ----------------------- -----------------------
  `id`                    UUID, PK                Unique ID of the
                                                  dead-letter record.

  `webhook_event_id`      UUID, FK                Original webhook event
                                                  that failed delivery.

  `merchant_id`           UUID, FK                Merchant associated
                                                  with the failed event.

  `final_error`           string                  Final error explaining
                                                  why delivery was moved
                                                  to the dead-letter
                                                  queue.

  `moved_at`              datetime                Time when the event
                                                  entered the dead-letter
                                                  queue.

  `replayed_at`           datetime                Time when an operator
                                                  or process replayed the
                                                  event, if replayed.
  -----------------------------------------------------------------------

**Interview explanation:** A dead-letter queue prevents a repeatedly
failing event from retrying forever. Operations teams can inspect the
failure, fix the cause, and replay the event when appropriate.

### 3.12 SETTLEMENT

**Purpose:** Stores a merchant's settlement batch and its financial
totals.

  -----------------------------------------------------------------------
  Attribute               Type / Key              Simple explanation
  ----------------------- ----------------------- -----------------------
  `id`                    UUID, PK                Unique ID of the
                                                  settlement batch.

  `merchant_id`           UUID, FK                Merchant receiving the
                                                  settlement.

  `gross_amount_paise`    long                    Total payment amount
                                                  included in the
                                                  settlement, in paise.

  `refund_amount_paise`   long                    Refund amount deducted
                                                  or accounted for, in
                                                  paise.

  `fee_amount_paise`      long                    Payment gateway fee, in
                                                  paise.

  `gst_amount_paise`      long                    GST amount on
                                                  applicable fees, in
                                                  paise.

  `net_amount_paise`      long                    Final net amount
                                                  payable to the
                                                  merchant, in paise.

  `status`                string                  Current settlement
                                                  processing status.

  `bank_reference`        string                  Bank reference for the
                                                  settlement transfer.

  `processed_at`          datetime                Time when the
                                                  settlement was
                                                  processed.

  `created_at`            datetime                Date and time when the
                                                  settlement record was
                                                  created.
  -----------------------------------------------------------------------

**Interview explanation:** Settlement is the payout/reconciliation
stage. It groups eligible payments and records gross amount, deductions,
fees, tax, and net payable amount.

### 3.13 SETTLEMENT_PAYMENT

**Purpose:** Join table that connects settlement batches with individual
payments.

  -----------------------------------------------------------------------
  Attribute               Type / Key              Simple explanation
  ----------------------- ----------------------- -----------------------
  `settlement_id`         UUID, PK + FK           Settlement batch
                                                  referenced by this
                                                  mapping row.

  `payment_id`            UUID, PK + FK           Payment included in
                                                  this settlement batch.
  -----------------------------------------------------------------------

**Interview explanation:** This is a composite-primary-key join table.
It represents a many-to-many relationship: one settlement can include
many payments, and a payment can be associated with a settlement
mapping. The composite key prevents the same payment from being added
twice to the same settlement.

### 3.14 REFUND

**Purpose:** Stores refund requests and their processing results.

  -----------------------------------------------------------------------
  Attribute               Type / Key              Simple explanation
  ----------------------- ----------------------- -----------------------
  `id`                    UUID, PK                Unique ID of the
                                                  refund.

  `payment_id`            UUID, FK                Payment against which
                                                  the refund is
                                                  requested.

  `merchant_id`           UUID, FK                Merchant that owns the
                                                  refund.

  `amount_paise`          long                    Refund amount stored in
                                                  paise.

  `status`                string                  Current refund status.

  `bank_reference`        string                  Reference returned by
                                                  the bank or processor.

  `error_code`            string                  Machine-readable refund
                                                  failure code.

  `error_description`     string                  Human-readable refund
                                                  failure reason.

  `processed_at`          datetime                Time when the refund
                                                  was processed.

  `created_at`            datetime                Date and time when the
                                                  refund record was
                                                  created.
  -----------------------------------------------------------------------

**Interview explanation:** Refunds are separate records because one
payment may have multiple partial refunds. Keeping each refund
independent provides traceability and makes reconciliation easier.

### 3.15 PAYMENT_TRANSITION_LOG

**Purpose:** Stores the history of payment status changes for auditing
and troubleshooting.

  -----------------------------------------------------------------------
  Attribute               Type / Key              Simple explanation
  ----------------------- ----------------------- -----------------------
  `id`                    UUID, PK                Unique ID of the
                                                  transition log entry.

  `payment_id`            UUID, FK                Payment whose status
                                                  changed.

  `from_status`           string                  Payment status before
                                                  the change.

  `to_status`             string                  Payment status after
                                                  the change.

  `event_type`            string                  Event that caused the
                                                  status transition.

  `actor`                 string                  User, service, or
                                                  system that performed
                                                  the action.

  `reason`                string                  Explanation for the
                                                  status change.

  `occurred_at`           datetime                Time when the
                                                  transition occurred.
  -----------------------------------------------------------------------

**Interview explanation:** This table provides an audit trail. Instead
of keeping only the latest payment status, the system can explain how
the payment reached that state.

------------------------------------------------------------------------

## 4. Entity relationships and cardinality

The following describes the relationships represented by the foreign
keys and the connectors in the diagram.

  ----------------------------------------------------------------------------------------------
  Parent entity     Child entity                Relationship      Explanation
  ----------------- --------------------------- ----------------- ------------------------------
  `MERCHANT`        `API_KEY`                   One-to-many       One merchant can have multiple
                                                                  API keys, for example
                                                                  test/live or rotated keys.

  `MERCHANT`        `APP_USER`                  One-to-many       One merchant can have multiple
                                                                  dashboard users.

  `MERCHANT`        `MERCHANT_WEBHOOK_CONFIG`   One-to-many       A merchant can configure one
                                                                  or more webhook
                                                                  destinations/configurations.

  `MERCHANT`        `CUSTOMER`                  One-to-many       A merchant can have many
                                                                  customers.

  `MERCHANT`        `ORDER_RECORD`              One-to-many       A merchant can create many
                                                (FK-based)        orders.

  `MERCHANT`        `PAYMENT`                   One-to-many       A merchant can own many
                                                (FK-based)        payment records.

  `ORDER_RECORD`    `PAYMENT`                   One-to-many       One order can have multiple
                                                                  payment attempts.

  `PAYMENT`         `REFUND`                    One-to-many       One payment can have multiple
                                                                  full or partial refund
                                                                  records.

  `PAYMENT`         `PAYMENT_TRANSITION_LOG`    One-to-many       One payment can have many
                                                                  status-history entries.

  `VAULT_CARD`      `CARD_TOKEN`                One-to-many       A vaulted card can be
                                                                  referenced by one or more
                                                                  tokens, subject to the
                                                                  tokenization policy.

  `CUSTOMER`        `CARD_TOKEN`                One-to-many       A customer can have multiple
                                                                  saved card tokens.

  `MERCHANT`        `CARD_TOKEN`                One-to-many       A merchant can have multiple
                                                                  tokens associated with its
                                                                  customers.

  `MERCHANT`        `WEBHOOK_EVENT`             One-to-many       A merchant can have many
                                                                  webhook events.

  `WEBHOOK_EVENT`   `DLQ_EVENT`                 One-to-zero/one   A webhook event may have a
                                                in the intended   dead-letter record if delivery
                                                flow              permanently fails.

  `MERCHANT`        `SETTLEMENT`                One-to-many       A merchant can receive
                                                                  multiple settlement batches
                                                                  over time.

  `SETTLEMENT`      `SETTLEMENT_PAYMENT`        One-to-many       A settlement can contain
                                                                  multiple payment mappings.

  `PAYMENT`         `SETTLEMENT_PAYMENT`        One-to-many in    A payment can appear in
                                                the join-table    settlement mapping records;
                                                mapping           business rules should prevent
                                                                  invalid duplicate or repeated
                                                                  settlement.
  ----------------------------------------------------------------------------------------------

### Why is `SETTLEMENT_PAYMENT` required?

A settlement contains multiple payments, and a payment needs to be
linked to the settlement in which it is paid out. Rather than storing a
list of payment IDs in one settlement row, the design uses a separate
mapping table.

Example:

``` text
SETTLEMENT
  settlement-101

SETTLEMENT_PAYMENT
  settlement-101 -> payment-501
  settlement-101 -> payment-502
  settlement-101 -> payment-503
```

This is a standard relational-database approach for representing a
many-to-many association.

------------------------------------------------------------------------

## 5. Overall payment flow

A simplified business flow through these entities is:

``` mermaid
flowchart TD
    A[Merchant is onboarded] --> B[API key is created]
    A --> C[Merchant configures webhook]
    C --> D[Customer is created]
    D --> E[Order is created]
    E --> F[Payment attempt is created]
    F --> G{Payment result}
    G -->|Success| H[Payment captured]
    G -->|Failure| I[Payment failed]
    H --> J[Payment transition log]
    I --> J
    H --> K[Webhook event created]
    I --> K
    K --> L{Merchant endpoint responds?}
    L -->|Yes| M[Webhook delivered]
    L -->|No| N[Retry delivery]
    N --> L
    N -->|Retries exhausted| O[Dead-letter event]
    H --> P[Settlement batch]
    P --> Q[Settlement-payment mapping]
    H --> R[Refund request, if needed]
```

### Step-by-step explanation

1.  **Merchant onboarding:** The platform creates a merchant record and
    stores its business and settlement details.
2.  **API authentication:** The merchant uses an API key to authenticate
    requests.
3.  **Customer and order creation:** The merchant identifies the buyer
    and creates an order with an amount in paise.
4.  **Payment attempt:** The gateway creates a payment linked to the
    order and merchant.
5.  **Payment processing:** The payment is processed through the
    selected payment method. Its status and bank reference or error
    details are updated.
6.  **Audit history:** Each important status change is recorded in
    `PAYMENT_TRANSITION_LOG`.
7.  **Webhook notification:** The gateway creates a `WEBHOOK_EVENT` and
    asynchronously sends it to the merchant.
8.  **Retry and DLQ:** If delivery fails, the event is retried. If
    retries are exhausted, it is moved to `DLQ_EVENT` for investigation
    or replay.
9.  **Settlement:** Eligible payments are grouped into a settlement
    batch. `SETTLEMENT_PAYMENT` identifies the payments included in that
    batch.
10. **Refund:** If a refund is requested, a separate `REFUND` record
    tracks its amount, status, bank reference, and processing result.

------------------------------------------------------------------------

## 6. Important backend and database design concepts

### 6.1 Multi-tenancy and data isolation

`merchant_id` appears in many tables. This associates records with the
merchant that owns them.

**Why it matters:** A merchant should only be able to access its own
customers, orders, payments, API keys, refunds, and settlements. Enforce
this in service authorization and, where appropriate, database
constraints or row-level security. Do not rely only on a client-supplied
`merchant_id`.

### 6.2 Idempotency

`ORDER_RECORD.idempotency_key` and `PAYMENT.idempotency_key` help the
system recognize repeated client requests.

**Example:** A client sends a payment request, but the network times out
before it receives the response. The client retries with the same
idempotency key. The backend should return the original result or safely
resume processing instead of creating a duplicate charge.

**Database recommendation:** Add a unique constraint scoped to the
appropriate merchant or operation, such as
`(merchant_id, idempotency_key)`, if that matches the API's idempotency
contract. The diagram does not mark these idempotency columns as `UK`,
so this is a recommended constraint rather than one explicitly shown.

### 6.3 Payment state management

A payment should move through valid states according to controlled
business rules. For example:

``` text
CREATED -> PENDING -> AUTHORIZED -> CAPTURED
                    \-> FAILED

CAPTURED -> PARTIALLY_REFUNDED -> REFUNDED
```

The exact states depend on the gateway and payment method. Validate
allowed transitions in the service layer and record each accepted
transition in `PAYMENT_TRANSITION_LOG`.

### 6.4 Webhook reliability

Webhook delivery should be asynchronous:

-   Persist the event before attempting delivery.
-   Sign the payload so the merchant can verify its authenticity.
-   Retry temporary failures with backoff.
-   Store the latest response code and retry schedule.
-   Move exhausted events to the dead-letter queue.
-   Make webhook consumers idempotent because the same event may be
    delivered more than once.

### 6.5 Card security and tokenization

The application should use `CARD_TOKEN` for normal card references and
keep sensitive card information inside the vault boundary.

-   Encrypt card data using an approved encryption design.
-   Protect encryption keys with a KMS or HSM where appropriate.
-   Apply strict access controls and audit access to card data.
-   Avoid logging PAN, CVV, secrets, or decrypted card values.
-   Follow applicable PCI DSS requirements. Tokenization does not
    automatically remove every system from PCI scope.

### 6.6 Money and reconciliation

Store monetary amounts as integer paise. Define clearly whether fees and
GST are included or excluded at each calculation stage. Reconciliation
should compare payment processor/bank references, refund records,
settlement totals, and the actual bank transfer.

------------------------------------------------------------------------

## 7. Suggested database constraints and indexes

These are practical recommendations to validate during implementation;
they are not all explicitly marked in the supplied diagram.

  -------------------------------------------------------------------------------------
  Table                      Suggested constraint or index      Reason
  -------------------------- ---------------------------------- -----------------------
  `API_KEY`                  Unique `key_id`; index             Fast credential lookup
                             `merchant_id`                      and merchant scoping.

  `APP_USER`                 Unique `(merchant_id, email)` or   Prevent duplicate
                             global unique email, based on      dashboard identities
                             login design                       within the chosen
                                                                scope.

  `ORDER_RECORD`             Unique                             Prevent duplicate order
                             `(merchant_id, idempotency_key)`   creation on retries.
                             when required by the API contract  

  `PAYMENT`                  Unique idempotency scope; indexes  Safe retries and
                             on `order_id`, `merchant_id`, and  efficient payment
                             `status`                           lookup/operations.

  `CARD_TOKEN`               Unique `token`; indexes on         Fast token lookup and
                             `customer_id` and `merchant_id`    ownership checks.

  `WEBHOOK_EVENT`            Index `(status, next_retry_at)`    Efficiently find events
                                                                due for retry.

  `DLQ_EVENT`                Index `merchant_id` and            Investigate and replay
                             `webhook_event_id`                 failed events.

  `REFUND`                   Index `(payment_id, status)`       Retrieve refunds and
                                                                calculate refunded
                                                                totals.

  `PAYMENT_TRANSITION_LOG`   Index `(payment_id, occurred_at)`  Fetch a payment's
                                                                history in
                                                                chronological order.

  `SETTLEMENT_PAYMENT`       Composite PK                       Prevent duplicate
                             `(settlement_id, payment_id)`      mapping rows in the
                                                                same settlement.
  -------------------------------------------------------------------------------------

Also consider:

-   Foreign-key constraints and deletion rules (`RESTRICT`, soft delete,
    or carefully controlled cascade).
-   Check constraints for non-negative amounts and valid expiry values.
-   Consistent status values, preferably enforced through enums, lookup
    tables, or validated application constants.
-   `NOT NULL` constraints for mandatory identifiers and financial
    fields.
-   Appropriate timestamp conventions, preferably UTC in storage.
-   Partitioning or archival strategies for high-volume event and audit
    tables.

------------------------------------------------------------------------

## 8. Interview-ready explanation

### Short answer: Explain this ER diagram

> "This is a database design for a multi-merchant payment gateway. The
> `MERCHANT` table is the root entity, and each merchant can have its
> own API keys, dashboard users, customers, orders, payments, webhook
> configuration, and settlements.
>
> When a merchant creates an order, we store it in `ORDER_RECORD`. A
> payment attempt is stored separately in `PAYMENT`, so one order can
> have multiple payment attempts. We use idempotency keys to protect
> against duplicate requests when clients retry.
>
> For every important payment status change, we insert a record into
> `PAYMENT_TRANSITION_LOG` to maintain an audit trail. If a payment
> succeeds or fails, we create a webhook event and deliver it
> asynchronously. Failed webhook deliveries are retried and eventually
> moved to a dead-letter queue if retries are exhausted.
>
> We also have a card vault and tokenization tables to avoid using raw
> card details throughout the application. Refunds are stored separately
> because a payment can have partial refunds. Finally, successful
> eligible payments are grouped into settlement batches, and
> `SETTLEMENT_PAYMENT` maps each settlement to its individual payments.
>
> Overall, the design separates the payment lifecycle,
> security-sensitive card storage, asynchronous notifications, refunds,
> and financial reconciliation into separate entities."

### Common follow-up interview questions

**1. Why do you maintain both `ORDER_RECORD` and `PAYMENT`?**

An order represents the request to collect money. A payment represents
an attempt to collect it. Separating them supports retries, multiple
payment methods, and payment failures without losing the original order.

**2. Why is idempotency important in a payment system?**

Clients may retry when a request times out or a response is lost.
Idempotency lets the backend recognize the same logical request and
prevents duplicate order or payment creation.

**3. Why store money in paise instead of `float` or `double`?**

Floating-point types can introduce precision errors. Integer paise gives
exact arithmetic for currency amounts, provided all calculations use
consistent rounding rules.

**4. Why do we need `PAYMENT_TRANSITION_LOG` if `PAYMENT` already has a
status?**

`PAYMENT.status` stores the latest state. The transition log stores the
complete history, including the previous state, new state, actor,
reason, and time.

**5. Why do we need a webhook event table?**

It makes notification delivery durable and independent of the payment
request. The system can retry delivery even if the merchant's endpoint
is temporarily down.

**6. What is a DLQ?**

A dead-letter queue stores messages that could not be processed or
delivered after the configured retry policy. It supports investigation
and controlled replay instead of silently losing events.

**7. Why use a separate `REFUND` table?**

A payment can be refunded in multiple partial transactions. Separate
refund rows preserve each refund's amount, status, bank reference, and
error details.

**8. What is the purpose of `SETTLEMENT_PAYMENT`?**

It maps payment records to settlement batches. It is a join table used
to represent the association without storing multiple payment IDs in one
column.

**9. How do you secure card information?**

Store card data only in a protected vault, encrypt sensitive values,
manage keys securely, use tokens in the application, restrict access,
avoid sensitive logs, and follow PCI DSS requirements.

**10. How do you prevent one merchant from accessing another merchant's
data?**

Derive the merchant identity from the authenticated API key or user
session, validate ownership in every service operation, scope queries by
`merchant_id`, and add database-level protections where appropriate.

------------------------------------------------------------------------

## 9. Design observations to validate before implementation

These are review points, not necessarily errors in the diagram:

1.  **Webhook secret storage:** `MERCHANT_WEBHOOK_CONFIG.webhook_secret`
    appears as a string. Confirm that it is encrypted or managed by a
    secrets service. The API key table stores a webhook secret hash, so
    the intended secret-handling strategy should be made consistent.
2.  **Idempotency uniqueness:** The diagram does not label either
    idempotency key as unique. Define the correct uniqueness scope and
    enforce it with a database constraint.
3.  **Payment-to-settlement rule:** Decide whether a payment can belong
    to only one settlement or can be re-settled after
    reversals/corrections. Enforce the chosen rule with constraints and
    business logic.
4.  **DLQ cardinality:** Decide whether a webhook event can have only
    one DLQ record or multiple dead-letter/replay attempts. Add a unique
    constraint on `webhook_event_id` if only one record is allowed.
5.  **Card-vault ownership:** `VAULT_CARD` has no visible `merchant_id`
    or `customer_id`; ownership is represented through `CARD_TOKEN`.
    Confirm that this matches the intended shared-vault and
    tenant-isolation model.
6.  **Refund totals:** Define how `PAYMENT.refunded_amount_paise` is
    updated and reconciled against the sum of successful
    `REFUND.amount_paise` rows. Use transactions or a reliable
    consistency strategy.
7.  **Order-to-payment consistency:** Because both `ORDER_RECORD` and
    `PAYMENT` carry `merchant_id`, validate that the payment's merchant
    matches the merchant on its referenced order.
8.  **Sensitive data:** Confirm retention, encryption, masking, access
    auditing, and deletion policies for PAN-related data, bank details,
    PAN/GST identifiers, secrets, and webhook payloads.

------------------------------------------------------------------------

## 10. Final summary

The schema is organized around the payment lifecycle:

-   **Merchant management:** `MERCHANT`, `APP_USER`, `API_KEY`,
    `MERCHANT_WEBHOOK_CONFIG`
-   **Buyer and order management:** `CUSTOMER`, `ORDER_RECORD`
-   **Payment processing:** `PAYMENT`, `PAYMENT_TRANSITION_LOG`
-   **Card security:** `VAULT_CARD`, `CARD_TOKEN`
-   **Notifications and reliability:** `WEBHOOK_EVENT`, `DLQ_EVENT`
-   **Refunds and payouts:** `REFUND`, `SETTLEMENT`,
    `SETTLEMENT_PAYMENT`

**One-line interview summary:** This schema separates merchant
onboarding, order creation, payment attempts, secure card tokenization,
reliable webhook delivery, refunds, and settlement reconciliation so
that each part of a payment gateway can be maintained, secured, and
scaled independently.
