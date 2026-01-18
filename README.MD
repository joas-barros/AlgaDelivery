# AlgaDelivery 📦 🚀

**AlgaDelivery** is a distributed system project built with **Java 21** and **Spring Boot 3**, demonstrating a robust **Microservices Architecture**.

The project simulates a logistics platform handling delivery orders, courier management, and real-time status tracking. It implements key distributed system patterns such as **Service Discovery**, **API Gateway**, **Event-Driven Architecture (Kafka)**, and **Resilience (Circuit Breaker & Retry)**.

## 🏗 Architecture & Modules

The system is composed of the following microservices:

| Service | Port | Description |
| --- | --- | --- |
| **Service Registry** | `8761` | **Eureka Server**. Handles service discovery and registration. |
| **Gateway** | `9999` | **Spring Cloud Gateway**. The entry point for the system. Handles routing, load balancing, and edge-level resilience. |
| **Delivery Tracking** | `Random` | Manages delivery orders, items, and preparation. Acts as a **Kafka Producer** and communicates synchronously with the Courier service for pricing. |
| **Courier Management** | `8081` | Manages couriers (drivers). Acts as a **Kafka Consumer** to automatically assign deliveries to available drivers. |

### Infrastructure (Docker)

* **PostgreSQL**: Primary database for business services.
* **Apache Kafka**: Message broker for asynchronous events.
* **Kafka UI**: Web interface for managing Kafka clusters.
* **pgAdmin**: Web interface for PostgreSQL.

---

## 🛠 Technology Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.4.1
* **Cloud Native:** Spring Cloud 2024.0.0 (Eureka, Gateway, LoadBalancer)
* **Messaging:** Spring for Apache Kafka
* **Resilience:** Resilience4j (Circuit Breaker, Retry)
* **Database:** PostgreSQL, Spring Data JPA
* **Tools:** Docker, Docker Compose, Lombok, Maven

---

## 🔄 Event-Driven Flow (Kafka)

The system uses an **Event-Driven Architecture** to decouple the delivery process from courier assignment.

1. **Topic:** `deliveries.v1.events`
2. **Flow:**
* When a delivery is **PLACED** in `Delivery Tracking`, a `DeliveryPlacedEvent` is published.
* `Courier Management` listens to this event and automatically **assigns** an available courier to the order.
* When a delivery is **FULFILLED**, a `DeliveryFulfilledEvent` is published, updating the courier's stats (updating the "last fulfilled delivery" timestamp).



---

## 🛡️ Resilience & Fault Tolerance

The project implements **Resilience4j** to handle failures gracefully:

1. **Circuit Breaker & Retry (Gateway Level):**
* Configured in the API Gateway to protect downstream services.


2. **Circuit Breaker & Retry (Service Level):**
* Implemented in `Delivery Tracking` when calling the `Courier Management` API for payout calculations.
* **Chaos Engineering:** The `CourierController` contains intentional random failures (`Math.random() < 0.5`) and delays to demonstrate the Circuit Breaker and Retry mechanisms in action.



---

## 🚀 Getting Started

### Prerequisites

* Java 21 JDK
* Docker & Docker Compose
* Maven

### 1. Start Infrastructure

Start the databases and Kafka broker using Docker Compose:

```bash
docker-compose up -d

```

### 2. Run the Microservices

It is recommended to run them in the following order:

1. **Service Registry** (`/Microservices/Service-Registry`)
2. **Courier Management** (`/Microservices/Courier-Management`)
3. **Delivery Tracking** (`/Microservices/Delevery-Tracking`)
4. **Gateway** (`/Microservices/Gateway`)

You can run each service using Maven wrapper:

```bash
./mvnw spring-boot:run

```

---

## 🔌 API Endpoints (via Gateway)

Base URL: `http://localhost:9999`

### Courier Management

* `POST /api/v1/couriers`: Register a new courier.
* `GET /api/v1/couriers`: List all couriers.
* `POST /api/v1/couriers/payout-calculation`: Calculate fee based on distance (contains simulated failures).

### Delivery Tracking

* `POST /api/v1/deliveries`: Draft a new delivery.
* *Payload includes sender, recipient, and items.*


* `PUT /api/v1/deliveries/{id}`: Edit delivery details.
* `POST /api/v1/deliveries/{id}/placement`: **Place the order** (Triggers Kafka Event).
* `POST /api/v1/deliveries/{id}/completion`: Mark as delivered (Triggers Kafka Event).

---

## 🔍 Monitoring & UI

| Service | URL | Credentials (if applicable) |
| --- | --- | --- |
| **Eureka Dashboard** | `http://localhost:8761` | - |
| **Kafka UI** | `http://localhost:8084` | - |
| **pgAdmin** | `http://localhost:8083` | `dba@algadelivery.com` / `algadelivery` |

---

## 👥 Credits

* **AlgaWorks** - *Educational Content & Inspiration* ([Youtube Channel](https://www.youtube.com/@algaworks))

## 📝 License

This project is licensed under the MIT License.