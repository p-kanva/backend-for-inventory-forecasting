# 📦 Inventory Forecasting Service

A **Spring Boot backend application** that helps businesses manage stock levels, forecast demand, and auto-generate purchase orders when inventory is low.  
It uses **JWT authentication**, **MySQL** for persistence, and is fully tested with **JUnit + Mockito**.

It includes modules for:
- User authentication & authorization
- Product management
- Sales tracking
- Forecast generation (moving average model)
- Purchase order automation
- Logging & audit trails
- Offline LLM-based text parsing (mock service for testing)


## Run locally (fastest path)
1. Create DB:
   ```sql
   CREATE DATABASE IF NOT EXISTS inventorydb;
   ```
2. Edit `src/main/resources/application.properties`:
   - Set `spring.datasource.username` and `spring.datasource.password` for your MySQL.
   - Replace `app.jwt.secret` with your own Base64 key:
     ```bash
     openssl rand -base64 32
     ```
3. Start the app:
   ```bash
   mvn spring-boot:run
   ```

## Smoke test (curl)
```bash
# Register ADMIN
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"username":"admin","email":"admin@example.com","password":"password","role":"ADMIN"}'

# Login
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"username":"admin","password":"password"}' | jq -r .token)

# Create product (ADMIN)
curl -X POST http://localhost:8080/api/products -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"name":"Blue Office Chair","category":"Furniture","currentStock":10,"reorderThreshold":20,"leadTimeDays":2}'

# Record a sale (recomputes forecast, decrements stock)
curl -X POST http://localhost:8080/api/sales -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"productId":1,"quantity":5,"location":"West warehouse","saleDate":"2025-07-15"}'

# Get product with latest forecast
curl http://localhost:8080/api/products/1 -H "Authorization: Bearer $TOKEN"

# List forecasts
curl "http://localhost:8080/api/forecasts?start=2025-07-01&end=2025-08-31" -H "Authorization: Bearer $TOKEN"

# Auto-generate purchase orders (ADMIN)
curl -X POST http://localhost:8080/api/orders/auto -H "Authorization: Bearer $TOKEN"

# Receive order
curl -X PUT http://localhost:8080/api/orders/1/receive -H "Authorization: Bearer $TOKEN"

# LLM parse (offline mock)
curl -X POST http://localhost:8080/api/llm/parse -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"data":"Sold 45 units of blue office chairs, Furniture category, on July 15th to the West warehouse."}'
```

## What "offline LLM" means
- We included `MockLLMService` which **does not call the internet**. It uses simple rules/regex to pull fields from messy text so you can test end-to-end locally.
- A real LLM (OpenAI/HF) would require an API key and HTTP calls. That's more accurate but needs internet and handling latency/errors/costs. There's a placeholder file `OpenAILLMService.java` indicating where that would go.

## Modules
- **Auth**: JWT, roles (ADMIN/USER), logout token blacklist
- **Products**: CRUD-lite (create, fetch with latest forecast)
- **Sales**: record sales (triggers forecast)
- **Forecasts**: moving average over last N days (`app.forecast.windowDays`)
- **Orders**: auto-generate when below threshold; receive to add stock
- **Logs**: each HTTP call recorded with correlation id

## Tests
- JUnit + Mockito: `ForecastServiceTest`, `OrderServiceTest`, `MockLLMServiceTest`, `JwtServiceTest`

## Future Improvements

Add ARIMA / ML models for advanced forecasting
Create frontend dashboard for visualization
Implement role-based access control
