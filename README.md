# Employee Management REST API

A production-ready RESTful backend for **employee lifecycle management** built with **Java 17**, **Spring Boot 3**, **Hibernate/JPA**, and **MySQL**.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.3.2 |
| ORM | Hibernate / Spring Data JPA |
| Database | MySQL 8.0 |
| Validation | Jakarta Bean Validation |
| Testing | JUnit 5, MockMvc, H2 (in-memory) |
| Build | Maven (with Maven Wrapper) |

---

## Architecture

```
src/main/java/com/employeemanagement/
├── controller/       # REST API endpoints
│   ├── EmployeeController.java
│   └── DepartmentController.java
├── service/          # Business logic interfaces
│   ├── EmployeeService.java
│   └── DepartmentService.java
│   └── impl/         # Implementations
│       ├── EmployeeServiceImpl.java
│       └── DepartmentServiceImpl.java
├── repository/       # Spring Data JPA repositories
│   ├── EmployeeRepository.java
│   └── DepartmentRepository.java
├── model/            # JPA entities
│   ├── Employee.java
│   ├── Department.java
│   └── EmployeeStatus.java
├── dto/              # Request / Response records
│   ├── EmployeeRequest.java
│   ├── EmployeeResponse.java
│   ├── DepartmentRequest.java
│   └── DepartmentResponse.java
└── exception/        # Custom exceptions & global handler
    ├── ResourceNotFoundException.java
    ├── ResourceAlreadyExistsException.java
    └── GlobalExceptionHandler.java
```

---

## Database Setup

1. Ensure MySQL 8 is running on `localhost:3306`.
2. The application will automatically create the database `employee_db` on first run.
3. Update credentials in `src/main/resources/application.properties` if needed:

```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

---

## Running the Application

```bash
# Clone the repository
git clone https://github.com/Elenal0/Employee-Management.git
cd Employee-Management

# Run using Maven Wrapper (no Maven installation required)
./mvnw spring-boot:run
```

The server starts at **http://localhost:8081**.

---

## API Endpoints

### Employee Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/employees` | Register a new employee |
| `GET` | `/api/employees` | Get all employees |
| `GET` | `/api/employees/{id}` | Get employee by ID |
| `GET` | `/api/employees/department/{deptId}` | Get employees by department |
| `GET` | `/api/employees/status/{status}` | Get employees by status (`ACTIVE`, `INACTIVE`, `TERMINATED`) |
| `PUT` | `/api/employees/{id}` | Update employee profile |
| `PUT` | `/api/employees/{id}/department/{deptId}` | Allocate employee to department |
| `DELETE` | `/api/employees/{id}` | Terminate employee |

### Department Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/departments` | Create a new department |
| `GET` | `/api/departments` | Get all departments |
| `GET` | `/api/departments/{id}` | Get department by ID |
| `GET` | `/api/departments/code/{code}` | Get department by code |

---

## Sample Requests

### Create a Department
```json
POST /api/departments
{
  "name": "Engineering",
  "code": "ENG"
}
```

### Register an Employee
```json
POST /api/employees
{
  "firstName": "Alice",
  "lastName": "Smith",
  "email": "alice@example.com",
  "phoneNumber": "9876543210",
  "hireDate": "2024-01-15",
  "jobTitle": "Software Engineer",
  "salary": 75000.00
}
```

### Allocate Employee to Department
```
PUT /api/employees/1/department/1
```

### Update Employee Profile
```json
PUT /api/employees/1
{
  "firstName": "Alice",
  "lastName": "Johnson",
  "email": "alice.johnson@example.com",
  "phoneNumber": "9876543210",
  "hireDate": "2024-01-15",
  "jobTitle": "Senior Software Engineer",
  "salary": 90000.00
}
```

---

## Running Tests

```bash
./mvnw clean test
```

Tests use an **H2 in-memory database** — no MySQL setup required for testing.

---

## Commit History

| # | Commit | Description |
|---|--------|-------------|
| 1 | `feat: initialize maven skeleton and project wrapper` | Maven project structure and wrapper |
| 2 | `feat: configure database connection and define JPA entities` | MySQL config and `Employee`/`Department` entities |
| 3 | `feat: create repository layer for database access` | Spring Data JPA repositories |
| 4 | `feat: implement business logic service layer` | Service interfaces and implementations |
| 5 | `feat: implement rest controllers, validation, and global exception handling` | REST API controllers, DTOs, exception handling |
| 6 | `test: add integration tests for CRUD endpoints` | MockMvc integration tests with H2 |
| 7 | `docs: add comprehensive readme documentation` | This README |
