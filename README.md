# Inventory Management System — Backend

A Spring Boot REST API with real-time WebSocket support for the Inventory Management System.

## 🚀 Features

- **JWT Authentication** — Secure stateless authentication
- **REST APIs** — Full CRUD operations for stock management
- **WebSocket Broadcasting** — Real-time stock updates via STOMP
- **Automated Reordering** — Scheduled reorder triggers for low/out-of-stock items
- **Multi-Warehouse Support** — Stock tracking across multiple warehouses
- **Audit Logging** — MongoDB audit trail for all stock changes
- **Optimistic Locking** — Concurrent stock update protection with @Version

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 3.2 |
| Security | Spring Security + JWT |
| Database | MySQL 8.0 |
| Audit Logs | MongoDB |
| Messaging | RabbitMQ |
| Caching | Redis |
| Real-Time | Spring WebSocket + STOMP |
| ORM | Spring Data JPA + Hibernate |
| Language | Java 17 |

## 📁 Project Structure
```
src/main/java/com/inventory/inventoryservice/
├── config/
│   ├── JwtUtil.java          # JWT token generation & validation
│   ├── JwtFilter.java        # JWT request filter
│   ├── SecurityConfig.java   # Spring Security configuration
│   └── WebSocketConfig.java  # WebSocket & STOMP configuration
├── controller/
│   ├── AuthController.java   # Login endpoint
│   ├── StockController.java  # Stock CRUD endpoints
│   ├── WarehouseController.java
│   └── AlertsController.java
├── service/
│   ├── InventoryService.java      # Service interface
│   ├── InventoryServiceImpl.java  # Business logic
│   └── StockBroadcastService.java # WebSocket broadcasting
├── repository/
│   ├── StockRepository.java    # JPA repository
│   ├── AuditLogRepository.java # MongoDB repository
│   └── UserRepository.java
├── entity/
│   ├── StockItem.java  # Stock entity with @Version
│   ├── AuditLog.java   # MongoDB document
│   └── User.java       # User entity
└── dto/
    ├── LoginRequest.java
    └── LoginResponse.java
```

## ⚙️ Prerequisites

- Java 17+
- MySQL 8.0
- MongoDB (optional — for audit logs)
- RabbitMQ (optional)
- Maven 3.6+

## 🗄️ Database Setup
```sql
CREATE DATABASE inventory_db;

USE inventory_db;

CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at DATETIME DEFAULT NOW()
);

INSERT INTO users (id, username, password, role) VALUES
('1', 'admin', '$2a$10$YOUR_BCRYPT_HASH', 'ADMIN');
```

## 🔧 Installation & Setup
```bash
# Clone the repository

cd inventory-service

# Update application.properties with your DB credentials
# Then build and run
mvn spring-boot:run
```

## ⚙️ Configuration

Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
spring.datasource.username=root
spring.datasource.password=yourpassword
server.port=8081
```

## 📡 API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/login` | Login and get JWT token |

### Stock Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/inventory/stock` | Get all stock items |
| POST | `/inventory/stock` | Create stock item |
| PUT | `/inventory/stock/{id}` | Update stock item |
| DELETE | `/inventory/stock/{id}` | Delete stock item |
| PATCH | `/inventory/stock/{productId}/adjust` | Adjust stock quantity |
| GET | `/inventory/stock/low-stock` | Get low stock items |
| GET | `/inventory/stock/out-of-stock` | Get out of stock items |

### Warehouses
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/inventory/warehouses` | Get all warehouses |
| GET | `/inventory/warehouses/{id}/stock` | Get stock by warehouse |

### Alerts
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/inventory/alerts` | Get all active alerts |

## 🔌 WebSocket

Connect to: `http://localhost:8081/ws`

Subscribe to topics:
- `/topic/stock` — All stock updates
- `/topic/stock/{warehouseId}` — Warehouse specific updates


