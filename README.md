# Ecom Backend

A Spring Boot backend for managing products in an e-commerce platform.

## 🛠️ Tech Stack

- Java 21
- Spring Boot 3.x
- Maven
- PostgreSQL (or your DB)

## 📦 Features

- CRUD APIs for managing products
- Image upload and retrieval
- Validation and error handling
- CORS configured for frontend integration

## 🚀 Running Locally

1️⃣ Clone the repository:
```bash
git clone https://github.com/SahilMaheshwari19/ecom-backend.git
cd ecom-backend
```
2️⃣ Configure your src/main/resources/application.properties:
````
spring.datasource.url=jdbc:postgresql://localhost:5432/ecomdb
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
````
3️⃣ Build and run:
````
./mvnw spring-boot:run
or run from your IDE.
````
4️⃣ The backend will be available at:
````
http://localhost:8080/api/
````

## 📄 API Endpoints

- GET /api/products - List all products
- GET /api/products/{id} - Get product by ID
- POST /api/product - Add a product with image
- PUT /api/product/{id} - Update a product (image optional)
- DELETE /api/product/{id} - Delete a product
- GET /api/products/{id}/image - Get product image by ID

### “This works with ecom-frontend” - available at - 
- https://github.com/SahilMaheshwari19/ecom-frontend