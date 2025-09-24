# 📦 Inventory Forecasting Service

A **Spring Boot backend application** that helps businesses manage stock levels, forecast demand, and auto-generate purchase orders when inventory is low.  
It uses **JWT authentication**, **MySQL** for persistence, and is fully tested with **JUnit + Mockito**.

---

## 🚀 Features
- **User Authentication**: Secure APIs with JWT.
- **Product Management**: Add, update, and manage products.
- **Sales Tracking**: Maintain sales history for accurate forecasting.
- **Demand Forecasting**: Uses Moving Average to predict stock requirements.
- **Purchase Order Automation**: Auto-generate purchase orders when stock falls below thresholds.

---

## 🛠️ Tech Stack
- **Backend**: Java 17, Spring Boot 3
- **Security**: Spring Security, JWT
- **Database**: MySQL
- **Testing**: JUnit 5, Mockito
- **Build Tool**: Maven

---

## 📂 Project Structure
src/
├── main/
│ ├── java/com/example/inventory/
│ │ ├── controller/     # REST controllers
│ │ ├── service/        # Business logic
│ │ ├── model/          # Entities
│ │ └── repository/     # Data access
│ └── resources/
│ ├── application.yml   # Configurations
│ └── schema.sql        # DB setup (if any)
└── test/               # Unit & integration tests

yaml


---

## ⚡ Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/p-kanva/inventory-forecasting-service.git
cd inventory-forecasting-service

2. Configure Database
Update application.yml with your MySQL username & password:

yaml

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/inventorydb
    username: root
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: update

      
3. Build & Run
bash

mvn clean install
mvn spring-boot:run
The service will start at: http://localhost:8080

🔑 API Endpoints (Sample)
Method	Endpoint	Description
POST	/auth/register	Register a new user
POST	/auth/login	User login (JWT token)
GET	/products	List all products
POST	/products	Add a new product
GET	/forecast/{productId}	Get demand forecast
POST	/purchase-orders/auto	Auto-generate purchase order

✅ Testing
Run tests with:
bash
mvn test

Includes:
Unit tests with Mockito

Integration tests with Spring Boot Test

📌 Future Improvements

Add ARIMA / ML models for advanced forecasting
Create frontend dashboard for visualization
Implement role-based access control
