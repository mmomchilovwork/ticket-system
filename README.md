# Ticket System

A lightweight backend for managing tickets in a public transport company: registering vehicles, issuing tickets, validating them and listing tickets per vehicle.

## Tech stack

- Java 8
- Java EE 8: JAX-RS, EJB, CDI, JPA, Bean Validation
- MicroProfile Health and Metrics (provided by WildFly)
- WildFly 18.0.1.Final
- H2 (in-memory) for local development
- Maven

## Prerequisites

| Tool | Version |
|---|---|
| JDK | 8 |
| Maven | 3.6+ (3.9.x recommended; Maven 4 requires Java 17) |
| WildFly | 18.0.1.Final |

WildFly 18 runs on Java 8 or 11 only. If a newer JDK is your default, set `JAVA_HOME` for WildFly in `bin/standalone.conf` (Linux/macOS) or `bin\standalone.conf.bat` (Windows).

## Getting started

### 1. Start WildFly

```
<WILDFLY_HOME>/bin/standalone.sh      # Linux/macOS
<WILDFLY_HOME>\bin\standalone.bat     # Windows
```

### 2. Configure the server (once per WildFly installation)

Run the provided CLI scripts while the server is running, in this order:

```
<WILDFLY_HOME>/bin/jboss-cli.sh --connect --file=scripts/configure-h2.cli
<WILDFLY_HOME>/bin/jboss-cli.sh --connect --file=scripts/configure-logging.cli
```

On Windows use `jboss-cli.bat`.

| Script | What it does |
|---|---|
| `configure-h2.cli` | Creates the in-memory H2 datasource `java:jboss/datasources/TicketSystemDS` and enables SQL logging (DEBUG) to `standalone/log/server.log` |
| `configure-logging.cli` | Adds the request id to the log format, sets the application log level and silences client-error logging of the JSON-B provider |
| 
The scripts are meant for a fresh WildFly installation; running them a second time fails with "already exists".

Verify the database connection:

```
<WILDFLY_HOME>/bin/jboss-cli.sh --connect --command="/subsystem=datasources/data-source=TicketSystemDS:test-connection-in-pool"
```

### 3. Build and deploy

```
mvn clean package wildfly:deploy-only
```

This deploys `target/ticket-system.war` to the running server via the management interface (`localhost:9990`). Alternatively, copy the WAR into `<WILDFLY_HOME>/standalone/deployments/`.

| What | URL |
|---|---|
| REST API | http://localhost:8080/ticket-system/api |
| OpenAPI (YAML) | http://localhost:8080/ticket-system/openapi.yaml |
| OpenAPI (JSON) | http://localhost:8080/ticket-system/openapi.json |
| Health | http://localhost:9990/health |
| Metrics | http://localhost:9990/metrics |

## API

| Method | Path | Description | Success |
|---|---|---|---|
| `POST` | `/vehicles` | Register a vehicle | 201 + `Location` |
| `GET` | `/vehicles/{id}` | Get a vehicle | 200 |
| `POST` | `/vehicles/{vehicleId}/tickets` | Issue a ticket for a vehicle | 201 + `Location` |
| `GET` | `/vehicles/{vehicleId}/tickets?page=0&size=20` | List tickets of a vehicle, newest first | 200 |
| `GET` | `/tickets/{code}` | Get a ticket by its code | 200 |
| `POST` | `/tickets/{code}/validation` | Validate a ticket | 200 |
| `GET` | `/logs?lines=100&requestId=...` | Tail of the server log (see [Monitoring](#monitoring)) | 200 |

Vehicle types: `BUS`, `TRAM`, `TROLLEYBUS`, `METRO`.

Ticket listing is paginated: `page` starts at 0 (default 0), `size` is 1–100 (default 20). The response contains `items`, `page`, `size` and `totalItems`.

### OpenAPI

The OpenAPI 3 definition is generated from the source code at build time by `swagger-maven-plugin` and packaged into the WAR as a static file. It is also available in `target/openapi/` after `mvn package`, so it can be inspected without a running server, for example in https://editor.swagger.io.

### Example session

```bash
BASE=http://localhost:8080/ticket-system/api

# Register a vehicle
curl -i -X POST $BASE/vehicles \
  -H "Content-Type: application/json" \
  -d '{"registrationNumber":"B1234AB","type":"BUS","capacity":80}'

# Issue a ticket (use the vehicle id from the previous response)
curl -i -X POST $BASE/vehicles/1/tickets \
  -H "Content-Type: application/json" \
  -d '{"passengerName":"John Doe"}'

# List tickets of the vehicle
curl -i "$BASE/vehicles/1/tickets?page=0&size=10"

# Validate a ticket (use the code from the issue response)
curl -i -X POST $BASE/tickets/<code>/validation

# Validating the same ticket again returns 409 Conflict
curl -i -X POST $BASE/tickets/<code>/validation
```

On Windows PowerShell use `curl.exe` instead of `curl`, or `Invoke-RestMethod`.

### Errors

Errors are returned as [RFC 7807 Problem Details](https://www.rfc-editor.org/rfc/rfc7807) with `Content-Type: application/problem+json`:

```json
{
  "title": "Conflict",
  "status": 409,
  "detail": "Ticket 3f2a... is already validated",
  "instance": "/tickets/3f2a.../validation",
  "timestamp": "2026-10-08T20:30:00.123Z"
}
```

| Status | When |
|---|---|
| 400 | Request validation failed (the response contains a `violations` list with `field` and `message`), or the request body is not valid JSON / contains a value of the wrong type |
| 404 | Vehicle or ticket not found, or unknown path |
| 405 / 415 | Wrong HTTP method or content type |
| 409 | Duplicate registration number (also under concurrent registration), ticket already validated, or concurrent modification |
| 500 | Unexpected error; the response contains a reference id that matches the entries in the server log |

## Monitoring

Monitoring combines the MicroProfile Health and Metrics implementations built into WildFly with application-specific checks and metrics.

### Health

Served by WildFly on the management port.

| Endpoint | Meaning |
|---|---|
| `GET http://localhost:9990/health/live` | Liveness: the server and the application respond. Does not check the database, so a database outage does not cause restarts. |
| `GET http://localhost:9990/health/ready` | Readiness: includes the application's `database` check (a connection is taken from the pool and validated). |
| `GET http://localhost:9990/health` | All checks. |

Status is `200` when all checks are `UP` and `503` otherwise. Failure details are written to the server log, not to the response.

### Metrics

Served by WildFly on the management port in Prometheus format, or as JSON with `Accept: application/json`.

| Endpoint | Content |
|---|---|
| `GET http://localhost:9990/metrics/application` | Application metrics (below) |
| `GET http://localhost:9990/metrics/vendor` | WildFly metrics: connection pool (`datasources`), HTTP requests and errors (`undertow`) |

Application metrics:

| Metric | Type | Meaning |
|---|---|---|
| `vehicles.registered` | Gauge | Number of registered vehicles |
| `tickets.issued` | Gauge | Number of issued tickets |
| `tickets.validated` | Gauge | Number of validated tickets |
| `vehicles.register`, `vehicles.find` | Timer | Count, rate and duration percentiles of the vehicle service operations |
| `tickets.issue`, `tickets.validate`, `tickets.find`, `tickets.list` | Timer | Count, rate and duration percentiles of the ticket service operations |

Gauges are read from the database at each scrape. Timers measure the service method inside the transaction; the full request time, including commit, is in the access log.

Example:

```
curl http://localhost:9990/metrics/application
curl -H "Accept: application/json" http://localhost:9990/metrics/application
```

### Logs

Every request under `/api` gets a request id: taken from the `X-Request-Id` request header if present and valid, otherwise generated. It is returned in the `X-Request-Id` response header and added to every log line of that request as `[req=...]`.

- One access log line per request: method, path, status and duration.
- Business events (vehicle registered, ticket issued, ticket validated) are logged at INFO. Passenger names are not logged.
- Unexpected errors (500) are logged at ERROR with the stack trace. The response contains only the request id as a reference.

```
2026-10-09 10:48:02,435 INFO  [com.example.ticketsystem.vehicle.VehicleService] (default task-1) [req=test-123] Vehicle registered: id=1, registrationNumber=CA7777AA, type=TRAM
2026-10-09 10:48:02,534 INFO  [com.example.ticketsystem.common.logging.RequestLoggingFilter] (default task-1) [req=test-123] POST /ticket-system/api/vehicles -> 201 (285 ms)
```

Accessing logs:

- **Application endpoint** `GET /api/logs?lines=100` returns the last lines of `server.log` (1–1000). With `requestId=<id>` it returns only the lines of that request, so the reference from a 500 response leads directly to its log entries. Stack trace lines carry no request id and appear only in the unfiltered view.
- **WildFly management** (password protected):
  ```
  <WILDFLY_HOME>/bin/jboss-cli.sh --connect --command="/subsystem=logging/log-file=server.log:read-log-file(lines=100)"
  ```

The `/api/logs` endpoint is a development and support aid. In production it must be restricted to operators or removed, and logs should be shipped to a central system (ELK, Loki, etc.).

### Management port

Health and metrics are on the management interface (port 9990), which WildFly binds to `127.0.0.1` by default. Exposing it outside the host requires `-bmanagement=<address>` and should be limited to the monitoring network, since the same interface serves the admin console.

## Design notes

- **Transactions** are container-managed: each public method of the `@Stateless` services runs in its own JTA transaction. Business exceptions are `@ApplicationException(rollback = true)`, so they roll back the transaction and reach the REST layer unwrapped.
- **Double validation** is prevented at two levels: the `Ticket` entity rejects a second validation, and optimistic locking (`@Version`) rejects concurrent validations of the same ticket.
- **Duplicate registration numbers** are rejected by a lookup before insert; concurrent duplicates that pass the lookup are caught by the database unique constraint and also returned as 409.
- **Ticket → Vehicle** is a unidirectional, lazy `@ManyToOne`. Tickets per vehicle are fetched with a paginated query instead of an unbounded collection on `Vehicle`.
- **Identifiers**: vehicles and tickets use sequence-based surrogate keys internally. Tickets are exposed through a random UUID `code`, so sequential ids are never part of the API.
- **Timestamps** are stored in UTC and returned in ISO-8601 format. The current time comes from an injected `java.time.Clock`.
- **Schema** is generated from the entities on deployment (`drop-and-create`). Data is lost on redeploy. A production setup would use Flyway/Liquibase migrations with schema validation; sequences must then be created with `INCREMENT BY 50` to match the entity mappings.
