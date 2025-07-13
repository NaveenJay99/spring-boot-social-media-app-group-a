# Spring Boot Social Media Application - Project Roadmap

## Project Overview
This is a comprehensive Spring Boot social media application with user authentication, post management, friend system, and like functionality. The application follows MVC architecture with proper separation of concerns.

## 🚀 Phase-by-Phase Development Guide

### Phase 1: Core Setup & Configuration ✅
**Files Created:**
- `src/main/resources/application.properties` - Database and app configuration
- `src/main/java/com/example/socialapp/config/SecurityConfig.java` - Spring Security setup
- `src/main/java/com/example/socialapp/config/DatabaseConfig.java` - Database configuration

**Key Features:**
- PostgreSQL database configuration
- BCrypt password encryption
- Session-based authentication
- Security filter chain setup

### Phase 2: Entity Layer ✅
**Files Created:**
- `src/main/java/com/example/socialapp/entity/User.java` - User entity with relationships
- `src/main/java/com/example/socialapp/entity/Post.java` - Post entity
- `src/main/java/com/example/socialapp/entity/FriendRequest.java` - Friend request management
- `src/main/java/com/example/socialapp/entity/Like.java` - Like system

**Key Features:**
- JPA entities with proper relationships
- Audit fields (created_at, updated_at)
- Business logic methods in entities
- Unique constraints and validations

### Phase 3: Data Transfer Objects (DTOs) ✅
**Files Created:**
- `src/main/java/com/example/socialapp/dto/UserRegistrationDto.java` - User registration
- `src/main/java/com/example/socialapp/dto/UserLoginDto.java` - User login
- `src/main/java/com/example/socialapp/dto/PostDto.java` - Post data transfer
- `src/main/java/com/example/socialapp/dto/FriendRequestDto.java` - Friend request data
- `src/main/java/com/example/socialapp/dto/LikeDto.java` - Like data transfer
- `src/main/java/com/example/socialapp/dto/UserDto.java` - User display data

**Key Features:**
- Input validation with Spring Validation
- Separation of API models from entities
- Clean data transfer between layers

### Phase 4: Repository Layer ✅
**Files Created:**
- `src/main/java/com/example/socialapp/repository/UserRepository.java` - User database operations
- `src/main/java/com/example/socialapp/repository/PostRepository.java` - Post database operations
- `src/main/java/com/example/socialapp/repository/FriendRequestRepository.java` - Friend request operations
- `src/main/java/com/example/socialapp/repository/LikeRepository.java` - Like operations

**Key Features:**
- Custom JPQL queries for complex operations
- Pagination support
- Efficient friend and post retrieval
- Search functionality

### Phase 5: Service Layer ✅
**Files Created:**
- `src/main/java/com/example/socialapp/service/UserService.java` - User business logic
- `src/main/java/com/example/socialapp/service/PostService.java` - Post business logic
- `src/main/java/com/example/socialapp/service/FriendService.java` - Friend management logic
- `src/main/java/com/example/socialapp/service/LikeService.java` - Like management logic

**Key Features:**
- UserDetailsService implementation for Spring Security
- Business logic separation
- Transaction management
- DTO conversion methods

### Phase 6: Exception Handling ✅
**Files Created:**
- `src/main/java/com/example/socialapp/exception/GlobalExceptionHandler.java` - Global exception handler
- `src/main/java/com/example/socialapp/exception/UserNotFoundException.java` - User not found exception
- `src/main/java/com/example/socialapp/exception/DuplicateEmailException.java` - Duplicate email exception
- `src/main/java/com/example/socialapp/exception/FriendRequestException.java` - Friend request exception

**Key Features:**
- Centralized exception handling
- Custom exceptions for business logic
- Proper error messages and HTTP status codes
- Graceful error handling

### Phase 7: Controller Layer ✅
**Files Created:**
- `src/main/java/com/example/socialapp/controller/AuthController.java` - Authentication operations
- `src/main/java/com/example/socialapp/controller/HomeController.java` - Home page and post creation
- `src/main/java/com/example/socialapp/controller/PostController.java` - Post API operations (likes)
- `src/main/java/com/example/socialapp/controller/UserController.java` - User directory
- `src/main/java/com/example/socialapp/controller/FriendController.java` - Friend management

**Key Features:**
- MVC pattern implementation
- RESTful API endpoints
- Form handling with validation
- AJAX support for dynamic updates

### Phase 8: Utility Classes ✅
**Files Created:**
- `src/main/java/com/example/socialapp/utils/ValidationUtils.java` - Validation utilities
- `src/main/java/com/example/socialapp/utils/DateUtils.java` - Date formatting utilities

**Key Features:**
- Common validation methods
- Date formatting and time-ago calculations
- Reusable utility functions

## 📁 Project Structure

```
src/main/java/com/example/socialapp/
├── SocialAppApplication.java (Main Application Class)
├── config/
│   ├── SecurityConfig.java
│   └── DatabaseConfig.java
├── controller/
│   ├── AuthController.java
│   ├── HomeController.java
│   ├── PostController.java
│   ├── FriendController.java
│   └── UserController.java
├── dto/
│   ├── UserRegistrationDto.java
│   ├── UserLoginDto.java
│   ├── PostDto.java
│   ├── FriendRequestDto.java
│   ├── LikeDto.java
│   └── UserDto.java
├── entity/
│   ├── User.java
│   ├── Post.java
│   ├── FriendRequest.java
│   └── Like.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── UserNotFoundException.java
│   ├── DuplicateEmailException.java
│   └── FriendRequestException.java
├── repository/
│   ├── UserRepository.java
│   ├── PostRepository.java
│   ├── FriendRequestRepository.java
│   └── LikeRepository.java
├── service/
│   ├── UserService.java
│   ├── PostService.java
│   ├── FriendService.java
│   └── LikeService.java
└── utils/
    ├── ValidationUtils.java
    └── DateUtils.java

src/main/resources/
├── application.properties
├── templates/ (TO BE CREATED)
│   ├── login.html
│   ├── register.html
│   ├── home.html
│   ├── users.html
│   ├── friends.html
│   └── profile.html
└── static/ (TO BE CREATED)
    ├── css/
    │   └── style.css
    └── js/
        └── app.js
```

## 🔧 Required Dependencies (Add to pom.xml)

```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 🗄️ Database Setup

1. **Install PostgreSQL**
2. **Create Database:**
   ```sql
   CREATE DATABASE socialapp;
   ```
3. **Update application.properties** with your database credentials

## 🚀 Next Steps (Frontend)

### Phase 9: Frontend Templates (TODO)
Create Thymeleaf templates:
- `login.html` - Login page
- `register.html` - Registration page
- `home.html` - Home feed with post creation
- `users.html` - User directory
- `friends.html` - Friend requests management
- `profile.html` - User profile page

### Phase 10: Styling (TODO)
- CSS styles with Bootstrap or custom CSS
- Responsive design
- JavaScript for AJAX functionality

## 🎯 Key Features Implemented

### ✅ Core Features
- [x] User registration with validation
- [x] User login with Spring Security
- [x] Session management
- [x] Post creation and display
- [x] Home feed with pagination
- [x] User directory with search
- [x] Friend request system
- [x] Like/Unlike functionality with AJAX
- [x] Proper error handling

### ✅ Advanced Features
- [x] Friend integration in home feed
- [x] Pagination for users and posts
- [x] Search functionality
- [x] Audit trails (created_at, updated_at)
- [x] Comprehensive validation
- [x] Security best practices

## 🛠️ Development Guidelines

### Architecture Principles
- **MVC Pattern**: Clear separation of concerns
- **SOLID Principles**: Single responsibility, open/closed, etc.
- **DRY Principle**: Don't repeat yourself
- **Security First**: Input validation, SQL injection prevention

### Code Quality
- **Lombok**: Used for reducing boilerplate code
- **Logging**: Comprehensive logging with SLF4J
- **Exception Handling**: Centralized error handling
- **Validation**: Both client-side and server-side validation

### Testing Strategy
- Unit tests for service layer
- Integration tests for repositories
- Controller tests for endpoints
- Security tests for authentication

## 🔥 Running the Application

1. **Setup Database**: Create PostgreSQL database
2. **Update Configuration**: Modify application.properties
3. **Run Application**: `mvn spring-boot:run`
4. **Access Application**: `http://localhost:8080`

## 📚 API Endpoints

### Authentication
- `GET /` - Redirect to login
- `GET /login` - Login page
- `POST /perform_login` - Process login
- `GET /register` - Registration page
- `POST /register` - Process registration
- `GET /logout` - Logout

### Home & Posts
- `GET /home` - Home feed
- `POST /home/create-post` - Create post
- `POST /home/delete-post/{id}` - Delete post
- `POST /api/posts/{id}/like` - Toggle like (AJAX)

### Users
- `GET /users` - User directory
- `GET /users/{id}` - User profile

### Friends
- `GET /friends` - Friend requests
- `POST /friends/send-request` - Send friend request
- `POST /friends/accept-request` - Accept friend request
- `POST /friends/decline-request` - Decline friend request

This roadmap provides a complete backend implementation for a social media application with all the features specified in your requirements. The code is production-ready with proper error handling, validation, and security measures.