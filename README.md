# Healthcare Management System

A RESTful backend application built with Java, Spring Boot, Spring Data JPA, Hibernate, and MySQL to manage healthcare operations, including patient records, doctor information, and appointment scheduling.

The project demonstrates backend development concepts such as MVC architecture, REST APIs, database relationships, input validation, exception handling, and appointment conflict prevention.

## Features

### 1. Patient Management
- Create patient records.
- Retrieve patient details.
- Update patient information.
- Delete patient records.
- Validate patient input.
- Store patient information in MySQL.

### 2. Doctor Management
- Create doctor records.
- Retrieve doctor details.
- Update doctor information.
- Delete doctor records.
- Persist doctor information using Spring Data JPA.

### 3. Appointment Management
- Book appointments for existing patients and doctors.
- Automatically calculate a 30-minute appointment duration.
- Check doctor availability before booking.
- Reject overlapping appointments for the same doctor.
- Allow consecutive appointments when one ends as another begins.
- Retrieve appointments by ID, doctor, or patient.
- Cancel appointments and release the associated time slot.
- Track appointment status: `BOOKED`, `CANCELLED`, or `COMPLETED`.

**Note:** Appointment conflict prevention is implemented through a database conflict check and a pessimistic lock on the doctor record. Booking requests should use the service layer to enforce these rules.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot | Application framework |
| Spring Web MVC | REST API development |
| Spring Data JPA | Database access |
| Hibernate | ORM and entity mapping |
| MySQL | Relational database |
| Maven | Dependency management and build |
| Jakarta Bean Validation | Request validation |
| Postman | API testing |
| Git and GitHub | Version control |

## Project Architecture

The application follows a layered MVC architecture.

```text
src/main/java/com/healthcare/hms/
├── controller/
│   ├── PatientController.java
│   ├── DoctorController.java
│   └── AppointmentController.java
├── dto/
│   └── AppointmentRequest.java
├── model/
│   ├── Patient.java
│   ├── Doctor.java
│   ├── Appointment.java
│   └── AppointmentStatus.java
├── repository/
│   ├── PatientRepository.java
│   ├── DoctorRepository.java
│   └── AppointmentRepository.java
├── service/
│   └── AppointmentService.java
|   └── PatientService.java
|   └── DoctorService.java
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── AppointmentConflictException.java
│   └── GlobalExceptionHandler.java
└── HmsApplication.java
```

The files above represent the intended structure. Adjust the tree to match the actual packages and files in your repository.

### Request Flow

```text
Client / Postman
       |
       v
REST Controller
       |
       v
Service Layer
       |
       v
Repository Layer
       |
       v
Hibernate / JPA
       |
       v
MySQL Database
```

- **Controller:** Receives HTTP requests and returns responses.
- **Service:** Implements business rules and coordinates operations.
- **Repository:** Reads and writes database records.
- **Entity:** Represents database tables and relationships.
- **DTO:** Defines the data accepted by selected API endpoints.
- **Exception handler:** Returns consistent error responses.

## Database Design

The application uses MySQL to persist patient, doctor, and appointment data.

### Appointment relationships

- Each appointment belongs to one patient.
- Each appointment belongs to one doctor.
- A patient or doctor can have multiple appointments.
- Cancelled appointments do not block future bookings.
- Appointment start and end times are stored in the database.

## Appointment Conflict Prevention

Appointments use a fixed duration of 30 minutes.

For example:

| Existing appointment | Requested appointment | Result |
|---|---|---|
| 10:00–10:30 | 10:15–10:45 | Rejected |
| 10:00–10:30 | 10:30–11:00 | Accepted |
| 10:00–10:30, cancelled | 10:00–10:30 | Accepted |

The overlap condition is:

```java
existingStart < requestedEnd
        && existingEnd > requestedStart
```

The application checks for an existing non-cancelled appointment for the same doctor. A transaction and pessimistic doctor-row lock help serialize concurrent booking attempts that use the same booking service.

## Prerequisites

Install the following before running the application:

- Java Development Kit (JDK) compatible with your Spring Boot version.
- Maven, or use the Maven wrapper included with the project.
- MySQL Server.
- IntelliJ IDEA or another Java IDE.
- Postman or another REST API client.

**Version note:** Verify the Java version required by the Spring Boot version configured in `pom.xml`. This project currently specifies Java 17 and Spring Boot 4.1.2; use a compatible JDK if your build requires a newer version.

## Setup and Installation

### 1. Clone the repository

```bash
git clone https://github.com/KothariAlok/healthcare-management-system.git
cd healthcare-management-system
```

Replace `KothariAlok` with your GitHub username.

### 2. Create the MySQL database

Open MySQL and run:

```sql
CREATE DATABASE healthcare_db;
```

Use the actual database name configured in your application's properties.

### 3. Configure database credentials

Configure your database connection in `src/main/resources/application.properties`.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/healthcare_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Set the `DB_USERNAME` and `DB_PASSWORD` environment variables in your IDE or execution environment before running the application.

Do not commit real database passwords, access tokens, or other secrets to GitHub.

`spring.jpa.hibernate.ddl-auto=update` is useful for local development. For production, use controlled database migrations and a safer schema-management strategy.

### 4. Run the application

Open the project in IntelliJ IDEA, reload Maven dependencies, and run `HmsApplication.java`.

Alternatively, if your project includes the Maven wrapper:

```bash
./mvnw spring-boot:run
```

On Windows Command Prompt:

```bat
mvnw.cmd spring-boot:run
```

The application normally starts at:

```text
http://localhost:8080
```

Confirm the startup logs show that Spring Boot started successfully and connected to MySQL.

## API Endpoints

Base URL:

```text
http://localhost:8080
```

### Patient APIs

The exact routes depend on the mappings in your `PatientController`.

| Method | Purpose |
|---|---|
| POST | Create a patient |
| GET | Retrieve patient records |
| GET | Retrieve a patient by ID |
| PUT | Update a patient |
| DELETE | Delete a patient |

Verify the actual paths in `PatientController.java` before publishing this section.

### Doctor APIs

The exact routes depend on the mappings in your `DoctorController`.

| Method | Purpose |
|---|---|
| POST | Create a doctor |
| GET | Retrieve doctor records |
| GET | Retrieve a doctor by ID |
| PUT | Update a doctor |
| DELETE | Delete a doctor |

Verify the actual paths in `DoctorController.java` before publishing this section.

### Appointment APIs

Base path: `/api/appointments`

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/appointments` | Book an appointment |
| GET | `/api/appointments` | Retrieve all appointments |
| GET | `/api/appointments/{id}` | Retrieve an appointment by ID |
| GET | `/api/appointments/doctor/{doctorId}` | Retrieve appointments for a doctor |
| GET | `/api/appointments/patient/{patientId}` | Retrieve appointments for a patient |
| PATCH | `/api/appointments/{id}/cancel` | Cancel an appointment |

### Example: Book an appointment

Request:

```http
POST /api/appointments
Content-Type: application/json
```

Body:

```json
{
  "patientId": 1,
  "doctorId": 1,
  "startTime": "2026-10-28T10:00:00",
  "reason": "General consultation"
}
```

The patient and doctor IDs must refer to existing records. The appointment start time must be in the future.

A successful booking returns `201 Created` with the saved appointment.

### Example: Conflicting booking

Submit another request for the same doctor with an overlapping start time:

```json
{
  "patientId": 1,
  "doctorId": 1,
  "startTime": "2026-10-28T10:15:00",
  "reason": "Follow-up consultation"
}
```

If the first appointment is still booked, the request should return `409 Conflict`.

### Example: Cancel an appointment

```http
PATCH /api/appointments/1/cancel
```

A successful cancellation changes the appointment status to `CANCELLED`, making its time slot available again.

## Error Handling

The API is designed to return meaningful HTTP status codes:

| Status code | Meaning |
|---|---|
| 201 Created | Appointment successfully booked |
| 200 OK | Request completed successfully |
| 400 Bad Request | Invalid request data |
| 404 Not Found | Patient, doctor, or appointment not found |
| 409 Conflict | Appointment overlaps an existing booking |

Actual error responses depend on the exception handling and validation implemented in the application.

## Testing

Use Postman to verify:

1. Patient and doctor CRUD operations.
2. Successful appointment booking.
3. Rejection of overlapping appointments.
4. Acceptance of back-to-back appointments.
5. Booking after cancellation.
6. Invalid patient and doctor IDs.
7. Invalid or past appointment start times.
8. Appointment retrieval by doctor and patient.

## Future Enhancements

Potential next improvements include:

- Spring Security and JWT authentication.
- Role-based authorization for admins, doctors, and patients.
- Search, sorting, and pagination.
- DTO-based responses for all API endpoints.
- Unit and integration tests with JUnit and Mockito.
- API documentation with Swagger/OpenAPI.
- Docker and Docker Compose.
- GitHub Actions CI/CD.
- Kafka-based appointment notifications.
- Doctor availability schedules and configurable appointment durations.
- Database migrations using Flyway or Liquibase.

## Learning Objectives

This project is intended to demonstrate practical understanding of:

- Java object-oriented programming.
- REST API design.
- Spring Boot MVC architecture.
- Dependency injection.
- Spring Data JPA and Hibernate.
- MySQL database relationships.
- Request validation and exception handling.
- Transaction management and pessimistic locking.
- Git and GitHub workflows.

## Author

**Alok Kothari**
- LinkedIn: www.linkedin.com/in/alok-kothari-4b3486253
- GitHub: https://github.com/KothariAlok
