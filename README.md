# URL Shortener Service

A robust and secure URL shortening service built with Spring Boot and MongoDB. This service allows users to create shortened URLs, track analytics, and manage their links efficiently.

## Features

- 🔐 User Authentication with JWT
- 🔗 URL Shortening with custom aliases
- 📊 URL Analytics (clicks, country-wise distribution)
- 🚀 Fast redirection service
- 🔍 URL validation and error handling
- 📱 RESTful API endpoints

## Tech Stack

- Java 17
- Spring Boot 3.x
- MongoDB
- Spring Security
- JWT Authentication
- Swagger/OpenAPI Documentation
- Gradle

## API Endpoints

### User Management

- `POST /api/v1/users/signup` - Register a new user
- `POST /api/v1/users/login` - User login (returns JWT token)
- `GET /api/v1/users/{userId}` - Get user details
- `PATCH /api/v1/users/password` - Update user password

### URL Management

- `POST /api/v1/short-url` - Create a short URL
- `POST /api/v1/short-url/custom` - Create a custom short URL with alias
- `GET /{shortUrl}` - Redirect to original URL

### Analytics

- `GET /api/v1/analytics/{hashedUrl}` - Get full analytics for a URL
- `GET /api/v1/analytics/country/{hashedUrl}` - Get clicks by country
- `GET /api/v1/analytics/click/{hashedUrl}` - Get total clicks
- `GET /api/v1/analytics/{userId}` - Get analytics by user

## Setup Instructions

### Prerequisites

- Java 17 or higher
- MongoDB
- Gradle

### Local Development Setup

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd url-shortener
   ```

2. Configure MongoDB:
   - Install MongoDB if not already installed
   - Create a database named `url_shortener`
   - Update MongoDB connection details in `application.properties` if needed

3. Build the project:
   ```bash
   ./gradlew build
   ```

4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080` 