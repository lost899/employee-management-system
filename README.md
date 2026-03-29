# Employee Management System

A secure backend application built with **Java**, **Spring Boot**, **Spring Security**, and **MySQL** to manage employee records with JWT-based authentication and authorization.

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL |
| Password Hashing | BCrypt |
| Testing | JUnit 5 + Mockito |
| Build Tool | Maven |

---

## 🏗️ Project Structure

```
src/
├── main/java/com/bipul/ems/
│   ├── controller/         # REST API Controllers
│   │   ├── AuthController.java
│   │   └── EmployeeController.java
│   ├── service/            # Business Logic Layer
│   │   ├── AuthService.java
│   │   └── EmployeeService.java
│   ├── repository/         # Data Access Layer (JPA Repositories)
│   │   ├── EmployeeRepository.java
│   │   └── UserRepository.java
│   ├── model/              # JPA Entities
│   │   ├── Employee.java
│   │   └── User.java
│   ├── dto/                # Data Transfer Objects
│   │   ├── EmployeeDTO.java
│   │   ├── AuthDTO.java
│   │   └── ApiResponse.java
│   ├── security/           # Security Configuration
│   │   ├── SecurityConfig.java
│   │   ├── JwtUtils.java
│   │   ├── JwtAuthFilter.java
│   │   └── CustomUserDetailsService.java
│   └── exception/          # Exception Handling
│       ├── ResourceNotFoundException.java
│       └── GlobalExceptionHandler.java
└── test/                   # JUnit Unit Tests
```

---

## ✨ Features

- ✅ **User Registration** with BCrypt password hashing
- ✅ **JWT-based Login** — stateless authentication
- ✅ **Role-based Authorization** — `ROLE_USER` and `ROLE_ADMIN`
- ✅ **CRUD Operations** on Employee records
- ✅ **Search Employees** by name, email, or department
- ✅ **Filter by Department**
- ✅ **Global Exception Handling** with meaningful error responses
- ✅ **Input Validation** on all request bodies
- ✅ **Controller–Service–Repository** layered architecture

---

## ⚙️ Setup & Run

### Prerequisites
- Java 17+
- MySQL 8+
- Maven 3.8+

### 1. Clone the repository
```bash
git clone https://github.com/your-username/employee-management-system.git
cd employee-management-system
```

### 2. Create MySQL Database
```sql
CREATE DATABASE employee_db;
```

### 3. Configure `application.properties`
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### 4. Run the application
```bash
mvn spring-boot:run
```

The server will start at `http://localhost:8080`

---

## 📡 API Endpoints

### 🔐 Auth APIs (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and get JWT token |

#### Register
```json
POST /api/auth/register
{
  "username": "bipul",
  "email": "bipul@example.com",
  "password": "password123"
}
```

#### Login
```json
POST /api/auth/login
{
  "username": "bipul",
  "password": "password123"
}
```
**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "bipul",
    "role": "ROLE_USER"
  }
}
```

---

### 👤 Employee APIs (Protected — requires Bearer Token)

Add the token to all requests:
```
Authorization: Bearer <your_jwt_token>
```

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| POST | `/api/employees` | Create employee | USER/ADMIN |
| GET | `/api/employees` | Get all employees | USER/ADMIN |
| GET | `/api/employees/{id}` | Get employee by ID | USER/ADMIN |
| PUT | `/api/employees/{id}` | Update employee | USER/ADMIN |
| DELETE | `/api/employees/{id}` | Delete employee | ADMIN only |
| GET | `/api/employees/search?keyword=` | Search employees | USER/ADMIN |
| GET | `/api/employees/department/{dept}` | Filter by department | USER/ADMIN |
| GET | `/api/employees/departments` | List all departments | USER/ADMIN |

#### Create Employee Example
```json
POST /api/employees
Authorization: Bearer <token>

{
  "firstName": "Rahul",
  "lastName": "Sharma",
  "email": "rahul@company.com",
  "department": "Engineering",
  "designation": "Backend Developer",
  "salary": 75000,
  "phoneNumber": "9876543210",
  "joiningDate": "2024-01-15"
}
```

---

## 🔒 Security

- Passwords are hashed using **BCrypt** before storing in the database
- JWT tokens expire after **24 hours**
- DELETE operations are restricted to **ADMIN** role only
- All other employee endpoints require valid **authentication**

---

## 🧪 Running Tests

```bash
mvn test
```

Tests cover:
- Employee creation (success + duplicate email)
- Fetch all employees
- Fetch by ID (found + not found)
- Delete employee (success + not found)

---

## 📝 Author

**Bipul Kumar**  
[linkedin.com/in/bipulkumar-hii](https://linkedin.com/in/bipulkumar-hii)  
kumar.bipul1620@gmail.com
