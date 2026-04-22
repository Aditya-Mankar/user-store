# User Store

A Spring Boot REST API that stores and retrieves user data using **Apache GemFire** as an in-memory distributed cache. The API accepts user data in **JSON**, **XML**, and **CSV** formats.

---

## Table of Contents

- [API Reference](#api-reference)
- [GemFire Setup](#gemfire-setup)
- [Running the Application](#running-the-application)
- [Running Tests](#running-tests)
- [Error Handling](#error-handling)
---


## API Reference

### Save User — JSON

```
POST /api/users
Content-Type: application/json
```

**Request body:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "address": {
    "street": "123 Main St",
    "city": "Mumbai",
    "country": "India"
  }
}
```

**Response `201 Created`:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "address": {
    "street": "123 Main St",
    "city": "Mumbai",
    "country": "India"
  }
}
```

---

### Save User — XML

```
POST /api/users
Content-Type: application/xml
```

**Request body:**
```xml
<user>
  <username>johndoe</username>
  <email>john@example.com</email>
  <address>
    <street>123 Main St</street>
    <city>Mumbai</city>
    <country>India</country>
  </address>
</user>
```

**Response `201 Created`** — same JSON shape as above.

---

### Save Users — CSV (Batch)

```
POST /api/users
Content-Type: multipart/form-data
```

Upload a `.csv` file using form field name `file`. The CSV must have the following header row:

```
username,email,street,city,country
johndoe,john@example.com,123 Main St,Mumbai,India
janedoe,jane@example.com,456 Park Ave,Pune,India
```

**Response `201 Created`:**
```json
[
  {
    "username": "johndoe",
    "email": "john@example.com",
    "address": { "street": "123 Main St", "city": "Mumbai", "country": "India" }
  },
  {
    "username": "janedoe",
    "email": "jane@example.com",
    "address": { "street": "456 Park Ave", "city": "Pune", "country": "India" }
  }
]
```

---

### Get User by Username

```
GET /api/users/{username}
```

| Parameter | Type | Description |
|---|---|---|
| `username` | Path variable | The unique username to look up |

**Response `200 OK`:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "address": {
    "street": "123 Main St",
    "city": "Mumbai",
    "country": "India"
  }
}
```

**Response `404 Not Found`** — if the username does not exist in the cache.

---

## GemFire Setup

This application runs in **client-server mode** and requires a running GemFire locator and server before startup.

### 1. Start the GemFire locator

```bash
gfsh> start locator --name=locator1 --port=10334
```

### 2. Start the GemFire server

```bash
gfsh> start server --name=server1 --locators=localhost[10334]
```

### 3. Create the Users region on the server

```bash
gfsh> create region --name=Users --type=REPLICATE
```

### 4. Verify setup

```bash
gfsh> list regions
gfsh> list members
```

Once GemFire is running, proceed to start the Spring Boot application.

---

## Running the Application

### Prerequisites

- Java 17+
- Maven 3.8+
- A running GemFire locator and server (see [GemFire Setup](#gemfire-setup))

### Clone and build

```bash
git clone <repository-url>
cd user-store
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

Or run the packaged jar:

```bash
java -jar target/user-store-0.0.1-SNAPSHOT.jar
```

The application starts on `http://localhost:8080` by default.

---

## Running Tests

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=UserServiceTest
mvn test -Dtest=CsvUserParserTest
mvn test -Dtest=UserControllerIntegrationTest
```

---

## Error Handling

All errors are handled centrally by `GlobalExceptionHandler` and return a consistent JSON response shape.

### Error response format

```json
{
  "code": "BAD_REQUEST",
  "message": "Username cannot be blank"
}
```

### Error codes

| HTTP Status | Code | Trigger |
|---|---|---|
| `400 Bad Request` | `BAD_REQUEST` | Invalid request data or malformed CSV |
| `400 Bad Request` | _(field map)_ | Bean validation failure — returns a map of `field → message` |
| `404 Not Found` | — | Username not found in cache |
| `500 Internal Server Error` | `INTERNAL_ERROR` | Unexpected server-side error |

### Validation errors (400) return a field-level map:

```json
{
  "username": "Username cannot be blank",
  "email": "Invalid email format"
}
```