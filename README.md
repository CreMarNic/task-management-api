# Task Management REST API

A comprehensive RESTful API for task management built with Spring Boot, featuring JWT authentication, user management, and full CRUD operations for tasks.

## 🚀 Features

- **User Authentication**: Register and login with JWT token-based authentication
- **Task Management**: Full CRUD operations for tasks
- **Task Filtering**: Filter tasks by status, priority, category, and due date
- **Task Search**: Search tasks by title or description
- **Security**: Spring Security with JWT tokens
- **API Documentation**: Swagger/OpenAPI integration
- **Database**: PostgreSQL/MySQL support
- **Validation**: Request validation with proper error handling

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** (JWT authentication)
- **Spring Data JPA**
- **PostgreSQL/MySQL**
- **Swagger/OpenAPI** (API documentation)
- **Maven**

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ or MySQL 8+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## 🔧 Setup Instructions

### 1. Clone the Repository

```bash
git clone <your-repo-url>
cd task-api
```

### 2. Database Setup

#### PostgreSQL:
```sql
CREATE DATABASE taskdb;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE taskdb TO postgres;
```

#### MySQL:
```sql
CREATE DATABASE taskdb;
CREATE USER 'taskuser'@'localhost' IDENTIFIED BY 'taskpass';
GRANT ALL PRIVILEGES ON taskdb.* TO 'taskuser'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure Application

Edit `src/main/resources/application.properties`:

```properties
# Update database credentials
spring.datasource.url=jdbc:postgresql://localhost:5432/taskdb
spring.datasource.username=postgres
spring.datasource.password=postgres

# Update JWT secret (use a strong random string in production)
jwt.secret=your-secret-key-change-this-in-production-use-a-strong-random-string
```

### 4. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access the Swagger UI at:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## 🔐 API Endpoints

### Authentication

#### Register User
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

#### Login
```
POST /api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "john_doe",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com"
}
```

### Tasks (Requires Authentication)

Add the JWT token to the Authorization header:
```
Authorization: Bearer <your-token>
```

#### Create Task
```
POST /api/tasks
Content-Type: application/json
Authorization: Bearer <token>

{
  "title": "Complete project documentation",
  "description": "Write comprehensive README and API docs",
  "status": "TODO",
  "priority": "HIGH",
  "dueDate": "2024-12-31",
  "category": "Work"
}
```

#### Get All Tasks
```
GET /api/tasks
Authorization: Bearer <token>
```

#### Get Tasks with Filters
```
GET /api/tasks?status=COMPLETED&priority=HIGH&category=Work
Authorization: Bearer <token>
```

#### Search Tasks
```
GET /api/tasks?search=documentation
Authorization: Bearer <token>
```

#### Get Task by ID
```
GET /api/tasks/{id}
Authorization: Bearer <token>
```

#### Update Task
```
PUT /api/tasks/{id}
Content-Type: application/json
Authorization: Bearer <token>

{
  "title": "Updated task title",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM"
}
```

#### Delete Task
```
DELETE /api/tasks/{id}
Authorization: Bearer <token>
```

## 📊 Task Status and Priority

### Task Status
- `TODO`
- `IN_PROGRESS`
- `COMPLETED`

### Task Priority
- `LOW`
- `MEDIUM`
- `HIGH`

## 🧪 Testing the API

### Using cURL

#### Register:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

#### Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser",
    "password": "password123"
  }'
```

#### Create Task (replace TOKEN with your JWT token):
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "title": "My first task",
    "description": "This is a test task",
    "priority": "HIGH"
  }'
```

### Using Postman

1. Import the collection (create requests for each endpoint)
2. Set up environment variables:
   - `base_url`: http://localhost:8080
   - `token`: (will be set after login)
3. Register/Login to get the token
4. Use the token in subsequent requests

## 📁 Project Structure

```
task-api/
├── src/
│   ├── main/
│   │   ├── java/com/marius/taskapi/
│   │   │   ├── TaskApiApplication.java
│   │   │   ├── controller/        # REST controllers
│   │   │   ├── service/          # Business logic
│   │   │   ├── repository/       # Data access layer
│   │   │   ├── model/            # Entity models
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── security/         # JWT & Security config
│   │   │   └── exception/        # Exception handling
│   │   └── resources/
│   │       └── application.properties
│   └── test/                     # Unit & integration tests
├── pom.xml
└── README.md
```

## 🔒 Security Features

- JWT-based authentication
- Password encryption with BCrypt
- Role-based access control (ready for extension)
- Secure token validation
- CORS configuration

## 🚀 Deployment

### Build for Production

```bash
mvn clean package -DskipTests
```

### Environment Variables

For production, use environment variables instead of hardcoded values:

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
jwt.secret=${JWT_SECRET}
```

### Deployment Platforms

- **Heroku**: Use Heroku Postgres addon
- **Railway**: Automatic PostgreSQL setup
- **Render**: PostgreSQL database service
- **AWS**: RDS PostgreSQL/MySQL
- **Docker**: Containerize the application

## 📝 Future Enhancements

- [ ] Task sharing/collaboration
- [ ] Task comments
- [ ] File attachments
- [ ] Email notifications
- [ ] Task templates
- [ ] Recurring tasks
- [ ] Task analytics/dashboard
- [ ] Unit and integration tests

## 🤝 Contributing

This is a portfolio project. Feel free to fork and enhance!

## 📄 License

This project is open source and available for portfolio purposes.

## 👤 Author

**Marius Cretu**
- Portfolio: [Your Portfolio URL]
- GitHub: [Your GitHub]
- LinkedIn: [Your LinkedIn]

---

Built with ❤️ using Spring Boot

