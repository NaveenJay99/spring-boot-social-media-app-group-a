# Social Media Application

A Spring Boot-based social media application where users can register, login, and create text posts.

## Features

- **User Authentication & Registration**
  - User registration with email, password, first name, and last name
  - Secure login with Spring Security
  - Password encryption using BCrypt
  - Session-based authentication

- **Post Management**
  - Create text-only posts (max 1000 characters)
  - View your own posts in chronological order
  - Real-time character counter

- **User Interface**
  - Responsive design using Bootstrap 5
  - Modern and clean UI
  - Form validation with error messages
  - Success/error notifications

## Technology Stack

- **Backend**: Spring Boot 3.x
- **Security**: Spring Security
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA (Hibernate)
- **Frontend**: Thymeleaf + Bootstrap 5
- **Validation**: Spring Boot Validation
- **Build Tool**: Maven

## Required Dependencies

The application uses the following Spring Boot starters:
- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-security`
- `spring-boot-starter-thymeleaf`
- `spring-boot-starter-validation`
- `postgresql` driver
- `lombok`

## Database Setup

1. Install PostgreSQL on your system
2. Create a database named `social_app_db`
3. Update the database credentials in `application.properties` if needed:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/social_app_db
   spring.datasource.username=postgres
   spring.datasource.password=123
   ```

## Running the Application

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd social-media-app
   ```

2. **Ensure PostgreSQL is running**
   - Start your PostgreSQL service
   - Verify the database `social_app_db` exists

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```
   Or if you're on Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

4. **Access the application**
   - Open your browser and go to `http://localhost:8080`
   - You'll be redirected to the login page

## Application Usage

### Registration
1. Click "Create Account" on the login page
2. Fill in your email, password, first name, and last name
3. Click "Create Account" to register
4. You'll be redirected to the login page with a success message

### Login
1. Enter your registered email and password
2. Click "Sign In"
3. You'll be redirected to the home feed page

### Creating Posts
1. On the home page, use the "Create New Post" section
2. Type your message (max 1000 characters)
3. Click "Post" to share your message
4. Your post will appear in your feed immediately

### Logout
1. Click the "Logout" button in the top navigation bar
2. You'll be redirected to the login page

## Project Structure

```
src/main/java/com/group/a/social_media_app/
├── SocialMediaAppApplication.java          # Main application class
├── config/
│   └── SecurityConfig.java                 # Spring Security configuration
├── controller/
│   ├── AuthController.java                 # Authentication endpoints
│   ├── HomeController.java                 # Home page controller
│   └── PostController.java                 # Post management
├── dto/
│   ├── LoginDto.java                       # Login form data
│   ├── PostDto.java                        # Post data transfer
│   └── UserRegistrationDto.java            # Registration form data
├── entity/
│   ├── Post.java                           # Post entity
│   └── User.java                           # User entity
├── repository/
│   ├── PostRepository.java                 # Post data access
│   └── UserRepository.java                 # User data access
└── service/
    ├── PostService.java                    # Post business logic
    └── UserService.java                    # User business logic

src/main/resources/
├── templates/
│   ├── home.html                           # Home feed page
│   ├── login.html                          # Login page
│   └── register.html                       # Registration page
└── application.properties                  # Application configuration
```

## Key Features Implementation

### Security
- Passwords are hashed using BCrypt
- Session-based authentication (no JWT required)
- Protected routes require authentication
- Proper logout functionality

### Validation
- Server-side validation using Spring Boot validation annotations
- Client-side validation with Bootstrap styling
- Custom error messages for validation failures

### Database Design
- User table with unique email constraint
- Post table with foreign key to user
- Automatic timestamp creation for posts and users

### UI/UX
- Responsive Bootstrap design
- Font Awesome icons
- Real-time character counter for posts
- Success/error message notifications
- Clean and modern interface

## Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running
- Verify database name and credentials in `application.properties`
- Check if the database `social_app_db` exists

### Port Already in Use
- If port 8080 is busy, change the port in `application.properties`:
  ```properties
  server.port=8081
  ```

### Build Issues
- Ensure you have Java 17+ installed
- Run `./mvnw clean install` to rebuild the project

## License

This project is created for educational purposes.