# Ticket System

A lightweight backend for managing tickets in a public transport company: registering vehicles, issuing tickets, validating them and listing tickets per vehicle.

## Tech stack

- Java 8
- Java EE 8: JAX-RS, EJB, CDI, JPA, Bean Validation
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

The application uses the datasource `java:jboss/datasources/TicketSystemDS`. Create it with the provided CLI script while the server is running:

```
<WILDFLY_HOME>/bin/jboss-cli.sh --connect --file=scripts/configure-h2.cli
```

On Windows use `jboss-cli.bat`. The script creates an in-memory H2 datasource and enables SQL logging to `standalone/log/server.log`.

Verify the connection:

```
<WILDFLY_HOME>/bin/jboss-cli.sh --connect --command="/subsystem=datasources/data-source=TicketSystemDS:test-connection-in-pool"
```

### 3. Build and deploy

```
mvn clean package wildfly:deploy-only
```

This deploys `target/ticket-system.war` to the running server via the management interface (`localhost:9990`). Alternatively, copy the WAR into `<WILDFLY_HOME>/standalone/deployments/`.

The application is available at:

```
http://localhost:8080/ticket-system/api
```

## API

| Method | Path | Description | Success |
|---|---|---|---|
| `POST` | `/vehicles` | Register a vehicle | 201 |
| `GET` | `/vehicles/{id}` | Get a vehicle | 200 |
| `POST` | `/vehicles/{vehicleId}/tickets` | Issue a ticket for a vehicle | 201 |
| `GET` | `/vehicles/{vehicleId}/tickets` | List tickets of a vehicle | 200 |
| `GET` | `/tickets/{code}` | Get a ticket by its code | 200 |
| `POST` | `/tickets/{code}/validation` | Validate a ticket | 200 |

Vehicle types: `BUS`, `TRAM`, `TROLLEYBUS`, `METRO`.

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
curl -i $BASE/vehicles/1/tickets

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
| 400 | Request validation failed; the response contains a `violations` list with `field` and `message` |
| 404 | Vehicle or ticket not found, or unknown path |
| 405 / 415 | Wrong HTTP method or content type |
| 409 | Duplicate registration number, ticket already validated, or concurrent modification |
| 500 | Unexpected error; the response contains a reference id that matches the entry in the server log |

## Design notes

- **Transactions** are container-managed: each public method of the `@Stateless` services runs in its own JTA transaction. Business exceptions are `@ApplicationException(rollback = true)`, so they roll back the transaction and reach the REST layer unwrapped.
- **Double validation** is prevented at two levels: the `Ticket` entity rejects a second validation, and optimistic locking (`@Version`) rejects concurrent validations of the same ticket.
- **Ticket → Vehicle** is a unidirectional, lazy `@ManyToOne`. Tickets per vehicle are fetched with a query instead of an unbounded collection on `Vehicle`.
- **Identifiers**: vehicles and tickets use sequence-based surrogate keys internally. Tickets are exposed through a random UUID `code`, so sequential ids are never part of the API.
- **Timestamps** are stored in UTC and returned in ISO-8601 format.
- **Schema** is generated from the entities on deployment (`drop-and-create`). Data is lost on redeploy. A production setup would use Flyway/Liquibase migrations with schema validation; sequences must then be created with `INCREMENT BY 50` to match the entity mappings.
