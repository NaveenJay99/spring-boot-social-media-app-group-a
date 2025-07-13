# Social Media Application

A comprehensive social media platform built with Spring Boot, featuring user authentication, post management, friend system, and like functionality.

## Features

### Core Features
- **User Authentication & Registration**
  - Secure user registration with email validation
  - BCrypt password hashing
  - Session-based authentication with Spring Security
  - Login/logout functionality

- **Post Management**
  - Create text-only posts
  - View personal and friends' posts in home feed
  - Post validation and content management
  - Chronological post ordering

- **Friend Management System**
  - Send friend requests to other users
  - Accept/decline incoming friend requests
  - View friend requests (sent and received)
  - User directory with pagination
  - Friend-based post filtering

- **Like/Unlike System**
  - Like and unlike posts
  - Dynamic like count display
  - Visual like status indicators
  - AJAX-based like operations

## Technology Stack

- **Backend:** Spring Boot 3.2.0
- **Security:** Spring Security 6.x with BCrypt
- **Database:** PostgreSQL with JPA/Hibernate
- **Frontend:** Thymeleaf + Bootstrap 5
- **Build Tool:** Maven
- **Java Version:** 17

## Project Structure

```
src/
├── main/
│   ├── java/com/socialmedia/
│   │   ├── SocialMediaApplication.java
│   │   ├── config/
│   │   │   └── SecurityConfig.java
│   │   ├── controller/
│   │   │   ├── AuthController.java
│   │   │   ├── HomeController.java
│   │   │   ├── PostController.java
│   │   │   ├── FriendController.java
│   │   │   └── UserController.java
│   │   ├── dto/
│   │   │   ├── UserRegistrationDto.java
│   │   │   ├── PostDto.java
│   │   │   ├── FriendRequestDto.java
│   │   │   └── UserDto.java
│   │   ├── entity/
│   │   │   ├── User.java
│   │   │   ├── Post.java
│   │   │   ├── FriendRequest.java
│   │   │   └── PostLike.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   ├── PostRepository.java
│   │   │   ├── FriendRequestRepository.java
│   │   │   └── PostLikeRepository.java
│   │   ├── service/
│   │   │   ├── UserService.java
│   │   │   ├── PostService.java
│   │   │   ├── FriendService.java
│   │   │   └── CustomUserDetailsService.java
│   │   └── exception/
│   │       ├── GlobalExceptionHandler.java
│   │       └── CustomExceptions.java
│   └── resources/
│       ├── application.properties
│       ├── static/
│       │   ├── css/
│       │   └── js/
│       └── templates/
│           ├── auth/
│           ├── home/
│           └── fragments/
```

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- IDE (IntelliJ IDEA, VS Code, or Eclipse)

### Database Setup
1. Install PostgreSQL and create a database:
```sql
CREATE DATABASE socialmedia;
```

2. Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/socialmedia
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application

1. **Clone the repository:**
```bash
git clone <repository-url>
cd social-media-app
```

2. **Install dependencies:**
```bash
mvn clean install
```

3. **Run the application:**
```bash
mvn spring-boot:run
```

4. **Access the application:**
   - Open your browser and navigate to `http://localhost:8080`
   - The application will redirect to the login page
   - Click "Register" to create a new account

## Usage Guide

### Getting Started
1. **Register a new account** with email, password, first name, and last name
2. **Login** with your credentials
3. **Create your first post** from the home feed
4. **Find other users** in the Users directory
5. **Send friend requests** to connect with other users
6. **Like posts** from your friends

### Key Pages
- **Home Feed** (`/home`): View your posts and friends' posts
- **Users Directory** (`/users`): Browse all users and send friend requests
- **Profile** (`/profile`): View your profile and posts
- **Friend Requests** (`/friends/requests`): Manage incoming and outgoing friend requests

## API Endpoints

### Authentication
- `GET /` - Redirect to login
- `GET /login` - Login page
- `POST /login` - Process login
- `GET /register` - Registration page
- `POST /register` - Process registration
- `GET /logout` - Logout

### Main Pages
- `GET /home` - Home feed
- `GET /users` - User directory
- `GET /profile` - User profile

### Posts
- `POST /posts` - Create new post
- `PUT /posts/{id}` - Update post
- `DELETE /posts/{id}` - Delete post
- `POST /posts/{id}/like` - Like/unlike post

### Friends
- `POST /friends/request/{userId}` - Send friend request
- `POST /friends/accept/{requestId}` - Accept friend request
- `POST /friends/decline/{requestId}` - Decline friend request
- `DELETE /friends/{userId}` - Remove friend

## Security Features

- **Authentication:** Session-based authentication with Spring Security
- **Authorization:** Role-based access control
- **Password Security:** BCrypt hashing
- **Input Validation:** Comprehensive validation on all forms
- **Session Management:** Secure session handling
- **CSRF Protection:** Can be enabled for production

## Database Schema

The application uses the following main entities:
- **User:** Stores user information and credentials
- **Post:** Stores user posts and content
- **FriendRequest:** Manages friend relationships
- **PostLike:** Tracks post likes

## Development Guidelines

### Code Quality
- Follows SOLID principles
- Uses repository pattern for data access
- Implements DTO pattern for data transfer
- Comprehensive error handling
- Proper logging and monitoring

### Best Practices
- Input validation at all levels
- Transaction management
- Proper exception handling
- Clean code structure
- Documentation and comments

## Future Enhancements

- Real-time notifications
- File upload support for posts
- Direct messaging system
- Advanced search functionality
- Mobile responsive design
- Email verification
- Password reset functionality

## Testing

Run tests using:
```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Support

For support or questions, please contact [your-email@example.com]