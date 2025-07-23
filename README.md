# PsychologySchedule
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://github.com/Luis-Parente/PsychologySchedule/blob/main/LICENSE)

This project was developed with a focus on the management system for professionals and scheduling. It was originally built for a psychology clinic but can be adapted for various types of services.
It provides endpoints for managing appointments and registering new consultations.
The project is currently being updated to include new features.

## 📋 Requirements

- Java 21+
- Git

## 🛠️ Installation & Execution

### 1.Clone the repository:
````bash
git clone https://github.com/Luis-Parente/PsychologySchedule
cd PsychologySchedule
````
### 2.Build the project:
````bash
./mvnw clean install
````
### 3.Run the application with:
````bash
./mvnw spring-boot:run
````
### Once running, the following resources will be available:

- API Base URL: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
- Swagger UI: http://localhost:8080/swagger-ui/index.html

  ## 🔍 Testing the API
You can test the API in two ways:

### 1.Swagger UI
Accessible at http://localhost:8080/swagger-ui/index.html. 
It provides a full list of available endpoints with detailed request/response schemas and example payloads.

### 2.Postman
Use Postman for a more flexible API testing experience.

The repository includes a Postman collection (PsychologySchedule.postman_collection.json)

- Go to File > Import
- Select the .json file from the repository
- You can then send requests directly using the pre-configured endpoints and data.

## 🧰 Tech Stack
- Java 21
- Spring Boot
- Spring Data JPA
- H2 Database (embedded)
- Maven Wrapper
- Swagger / OpenAPI
- Postman (testing)
