# Bug Report: Social Media App

## Overview
This Spring Boot social media application has several critical bugs and incomplete implementations that would prevent it from compiling and running successfully.

## Critical Bugs

### 1. **Missing Imports in UserService.java**
**File**: `src/main/java/com/group/a/social_media_app/service/UserService.java`
**Issue**: The UserService class uses annotations and classes without importing them:
- `@Service` - needs `import org.springframework.stereotype.Service;`
- `@RequiredArgsConstructor` - needs `import lombok.RequiredArgsConstructor;`
- `@Slf4j` - needs `import lombok.extern.slf4j.Slf4j;`
- `@Transactional` - needs `import org.springframework.transaction.annotation.Transactional;`
- `UserRepository`, `PasswordEncoder`, `User`, `UserRegistrationDTO`, `UserAlreadyExistsException`
- `Optional` - needs `import java.util.Optional;`

**Impact**: Compilation failure

### 2. **Incorrect Exception Filename**
**File**: `src/main/java/com/group/a/social_media_app/exception/serAlreadyExistsException.java`
**Issue**: Filename is missing "U" - should be `UserAlreadyExistsException.java`
**Impact**: Class cannot be found, compilation failure

### 3. **Empty Entity Classes**
**Files**: 
- `src/main/java/com/group/a/social_media_app/entity/User.java`
- `src/main/java/com/group/a/social_media_app/entity/Post.java`

**Issue**: Entity classes are empty and missing:
- JPA annotations (`@Entity`, `@Id`, `@GeneratedValue`, etc.)
- Field definitions
- Constructors, getters, setters
- Relationships between entities

**Impact**: UserService references methods that don't exist

### 4. **Empty Repository Interfaces**
**Files**: 
- `src/main/java/com/group/a/social_media_app/repository/UserRepository.java`
- `src/main/java/com/group/a/social_media_app/repository/PostRepository.java`

**Issue**: Repository interfaces are empty and missing:
- Extension of `JpaRepository<Entity, ID>`
- Custom query methods referenced in UserService

**Impact**: UserService cannot access database methods

### 5. **Empty DTO Classes**
**Files**: 
- `src/main/java/com/group/a/social_media_app/dto/UserRegistrationDTO.java`
- `src/main/java/com/group/a/social_media_app/dto/UserLoginDTO.java`
- `src/main/java/com/group/a/social_media_app/dto/PostDTO.java`

**Issue**: DTO classes are empty and missing:
- Field definitions
- Validation annotations
- Methods like `isPasswordMatching()`, `getEmail()`, etc.

**Impact**: UserService references non-existent methods

### 6. **Empty Controller Classes**
**Files**: 
- `src/main/java/com/group/a/social_media_app/controller/AuthController.java`
- `src/main/java/com/group/a/social_media_app/controller/PostController.java`
- `src/main/java/com/group/a/social_media_app/controller/HomeController.java`

**Issue**: Controllers are empty and missing:
- REST endpoints
- Request mappings
- Service injection

**Impact**: No API endpoints available

### 7. **Empty Exception Handler**
**File**: `src/main/java/com/group/a/social_media_app/exception/GlobalExceptionHandler.java`
**Issue**: Exception handler is empty but UserService throws custom exceptions
**Impact**: Unhandled exceptions will cause 500 errors

### 8. **Empty Security Configuration**
**File**: `src/main/java/com/group/a/social_media_app/config/SecurityConfig.java`
**Issue**: Security configuration is empty but Spring Security is included in dependencies
**Impact**: Default security configuration may not meet requirements

### 9. **Java Version Issue**
**File**: `pom.xml` (line 29)
**Issue**: Specifies Java version 24 which doesn't exist
```xml
<java.version>24</java.version>
```
**Should be**: Java 17, 21, or another existing LTS version
**Impact**: Build failure

### 10. **Incomplete Maven Configuration**
**File**: `pom.xml` (lines 85-90)
**Issue**: Lombok annotation processor path is missing version
**Impact**: Lombok annotations may not be processed correctly

## Additional Issues

### 11. **Missing Service Implementations**
- `PostService.java` - empty class
- `CustomUserDetailService.java` - empty class (should implement UserDetailsService)

### 12. **Missing Utility Implementation**
- `ValidationUtils.java` - empty class

### 13. **Inconsistent Exception Naming**
- File named `serAlreadyExistsException.java` but referenced as `UserAlreadyExistsException` in UserService

## Recommendations

1. **Immediate Actions**:
   - Fix the exception filename
   - Add missing imports to UserService
   - Set correct Java version in pom.xml

2. **Implementation Required**:
   - Implement all entity classes with JPA annotations
   - Implement repository interfaces extending JpaRepository
   - Implement DTO classes with proper fields and validation
   - Implement controller classes with REST endpoints
   - Implement security configuration
   - Implement exception handler

3. **Testing**:
   - Add unit tests for service layer
   - Add integration tests for controllers
   - Test database operations

## Severity Assessment
- **Critical**: 10 issues (prevents compilation/runtime)
- **High**: 3 issues (missing core functionality)
- **Medium**: 0 issues

This codebase appears to be a skeleton/template with mostly empty implementations and requires significant development work to become functional.