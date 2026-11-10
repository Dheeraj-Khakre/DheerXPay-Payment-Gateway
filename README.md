# DHEERXPAY (Razorpay-Payment-Gateway-clone)
_Built a distributed payment processing system (7 microservices, Kubernetes-native) implementing idempotency, transactional outbox, and distributed locking patterns to guarantee exactly-once payment processing under concurrent load_

## Run NeuraPay locally

Requires Java 25 and Docker Compose. From the repository root, start PostgreSQL and the Spring Boot application:

```powershell
cd NeuraPay
docker compose up -d postgres
.\mvnw.cmd spring-boot:run
```

To build a runnable JAR, run `.\mvnw.cmd clean package` and then
`java -jar target\DheerX-Pay-0.0.1-SNAPSHOT.jar`. Database connection settings can be overridden with
`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, and `DB_PASSWORD`.
