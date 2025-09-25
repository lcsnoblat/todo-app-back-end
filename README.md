# Shopping List API

A Spring Boot REST API for managing shopping list items.

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Maven** (build tool)
- **Docker** (containerization)

## API Endpoints

### Basic CRUD Operations

- `GET /api/items` - Get all items (supports filtering)
- `GET /api/items/{id}` - Get item by ID
- `POST /api/items` - Create new item
- `PUT /api/items/{id}` - Update existing item
- `DELETE /api/items/{id}` - Delete item

### Advanced Endpoints

- `GET /api/items?category={category}` - Filter by category
- `GET /api/items?search={name}` - Search by name (case-insensitive)
- `GET /api/items?minCost={amount}` - Filter by minimum cost
- `GET /api/items/categories` - Get all unique categories
- `GET /api/items/total-cost` - Get total shopping cost

### Example Request/Response

**Create Item:**
```bash
curl -X POST http://localhost:8080/api/items \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Organic Apples",
    "price": 4.99,
    "quantity": 5,
    "category": "Fruits"
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "Organic Apples",
  "price": 4.99,
  "quantity": 5,
  "category": "Fruits",
  "cost": 24.95,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

## How to Run

**Prerequisites:** Java 17+ is required

### Method 1: Using Maven Wrapper (Recommended)
```bash
# Clone or download the project
cd shopping-list-api

# Run the application
./mvnw spring-boot:run

# Or build and run the jar
./mvnw clean package
java -jar target/shopping-list-api-0.0.1-SNAPSHOT.jar
```

### Method 1b: If you have Maven installed
```bash
# Run the application
mvn spring-boot:run

# Or build and run the jar
mvn clean package
java -jar target/shopping-list-api-0.0.1-SNAPSHOT.jar
```

### Method 2: Using Docker
```bash
# Build the application first
./mvnw clean package

# Build Docker image
docker build -t shopping-list-api .

# Run the container
docker run -p 8080:8080 shopping-list-api
```

### Method 3: Using Docker Compose (Optional)
```bash
# Create a docker-compose.yml if needed
docker-compose up --build
```

The application will start on `http://localhost:8080`

## Testing the API

### Using curl:
```bash
# Get all items
curl http://localhost:8080/api/items

# Get total cost
curl http://localhost:8080/api/items/total-cost

# Search items
curl "http://localhost:8080/api/items?search=banana"
```

### Using H2 Console:
Access the database console at `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:shoppinglist`
- Username: `sa`
- Password: `password`

## Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ShoppingItemServiceTest

# Run integration tests
./mvnw test -Dtest=ShoppingItemControllerIntegrationTest
```

## Project Structure

```
src/
├── main/java/com/example/shoppinglist/
│   ├── ShoppingListApplication.java          # Main application class
│   ├── entity/ShoppingItem.java              # JPA entity with computed fields
│   ├── repository/ShoppingItemRepository.java # Custom repository methods
│   ├── service/ShoppingItemService.java      # Business logic layer
│   ├── controller/ShoppingItemController.java # REST endpoints
│   ├── config/DataLoader.java                # JSON data loading
│   └── exception/
│       ├── ItemNotFoundException.java        # Custom exception
│       └── GlobalExceptionHandler.java       # Global error handling
├── main/resources/
│   ├── application.yml                       # Application configuration
│   └── sample-data.json                      # Initial data
└── test/java/com/example/shoppinglist/
    ├── service/ShoppingItemServiceTest.java  # Unit tests
    └── controller/ShoppingItemControllerIntegrationTest.java # Integration tests
```

