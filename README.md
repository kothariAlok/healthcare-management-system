# Healthcare Management System

A backend REST API application built using Java, Spring Boot, Spring Data JPA, Hibernate, and MySQL. The project demonstrates core backend development concepts through the management of patient and doctor records.

## Overview

The Healthcare Management System provides REST endpoints for managing healthcare data. It is being developed as a learning and portfolio project to demonstrate REST API development, database integration, and layered application architecture.

## Features

### Patient Management
- Create patient records
- Retrieve patient information
- Update existing patient records
- Search patient by keyword
- Delete patient records

### Doctor Management
- Create doctor records
- Retrieve doctor information
- Update existing doctor records
- Search doctor by keyword
- Delete doctor records

*Include only the features implemented and tested in current version.*

## Technology Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Persistence:** Spring Data JPA, Hibernate
- **Database:** MySQL
- **Build Tool:** Maven
- **API Testing:** Postman
- **Version Control:** Git and GitHub

## Application Architecture

The application follows a layered architecture:

```text
Client (Postman)
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
MySQL Database
```

- **Controller:** Receives HTTP requests and returns responses.
- **Service:** Contains business logic.
- **Repository:** Handles database operations using Spring Data JPA.
- **Entity:** Represents database records.

## API Documentation

Base URL for local development:

`http://localhost:8080`

### Patient APIs

The following table assumes the base path `/api/patients`. Update it to match your actual controller mappings.

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/patients` | Create a patient |
| GET | `/api/patients` | Retrieve all patients |
| GET | `/api/patients/{id}` | Retrieve a patient by ID |
| PUT | `/api/patients/{id}` | Update a patient |
| DELETE | `/api/patients/{id}` | Delete a patient |

### Doctor APIs

The following table assumes the base path `/api/doctors`. Include these endpoints only after implementing and testing them.

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/doctors` | Create a doctor |
| GET | `/api/doctors` | Retrieve all doctors |
| GET | `/api/doctors/{id}` | Retrieve a doctor by ID |
| PUT | `/api/doctors/{id}` | Update a doctor |
| DELETE | `/api/doctors/{id}` | Delete a doctor |

## Example API Request

Example request to create a patient. Replace the fields with the fields defined in your actual `Patient` entity.

**POST** `/api/patients`

```json
{
  "firstName": "Rahul",
  "lastName": "Sharma",
  "dateOfBirth": "1996-05-15",
  "gender": "Male",
  "phoneNumber": "9876543210",
  "email": "rahul.sharma@example.com",
  "address": "Andheri East, Mumbai",
  "bloodGroup": "O+",
  "emergencyContact": "9876501234",
  "medicalHistory": "No significant medical history",
  "allergies": "Penicillin"
}
```

Example only. The request body must match the fields accepted by your application.

## Database Configuration

The application uses MySQL for persistent data storage.

Configure your local database connection using environment variables:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/healthcare_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Replace the database name with your local database name if necessary. Do not commit real database passwords or other credentials to GitHub.

## Getting Started

### Prerequisites

- Java version compatible with the project configuration
- MySQL Server
- Maven, or the Maven Wrapper included in the project
- Git

### Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/kothariAlok/healthcare-management-system.git
   ```

2. Navigate into the project directory:

   ```bash
   cd healthcare-management-system
   ```

3. Create the MySQL database if it does not already exist:

   ```sql
   CREATE DATABASE healthcare_db;
   ```

4. Configure the required environment variables `DB_USERNAME` and `DB_PASSWORD` in your IDE or terminal.

5. Run the application using the Maven Wrapper on Windows:

   ```bat
   mvnw.cmd spring-boot:run
   ```

6. Test the implemented API endpoints using Postman.

## Future Enhancements

Potential improvements include:

- Request validation
- Global exception handling
- Appointment management
- Patient-doctor relationships
- Authentication and role-based authorization
- Unit and integration tests

These are planned enhancements, not claims about currently implemented functionality.

## Project Status

This project is under active development. Features and API documentation will be updated as additional modules are implemented and tested.

## Author

Alok Kothari

GitHub: https://github.com/kothariAlok
