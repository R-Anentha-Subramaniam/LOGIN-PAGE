/*
  LOGIN API CONTROLLER - Java Spring Boot REST API
  
  INSTRUCTIONS FOR NON-CODERS:
  1. This creates web APIs that the frontend can call
  2. Install Spring Boot framework to use this code  
  3. This handles HTTP requests from the login form
  4. Connects the React frontend with the Java database code
  
  SETUP REQUIREMENTS:
  - Spring Boot framework
  - Spring Web dependency
  - DatabaseConnection.java file from previous code
*/

package com.college.booking.controller;

import com.college.booking.database.DatabaseConnection;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.HashMap;

/**
 * LOGIN API CONTROLLER
 * This class creates web endpoints that the frontend login form can call
 * It receives HTTP requests and returns JSON responses
 */
@RestController                                    // Tells Spring this is a REST API controller
@RequestMapping("/api/auth")                       // Base URL path for all endpoints in this controller
@CrossOrigin(origins = "http://localhost:3000")   // CHANGEABLE: Allow frontend to call these APIs
public class LoginController {
    
    /**
     * LOGIN ENDPOINT - Handles faculty login requests
     * 
     * URL: POST /api/auth/login
     * This endpoint receives username and password from frontend
     * and returns success/failure response
     * 
     * @param loginRequest Map containing username and password
     * @return JSON response with login result
     */
    @PostMapping("/login")                         // HTTP POST method for login
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        
        // RESPONSE MAP - Will contain the JSON response data
        Map<String, Object> response = new HashMap<>();
        
        try {
            // EXTRACT LOGIN DATA - Get username and password from request
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");
            String rememberMe = loginRequest.get("rememberMe"); // Optional remember me flag
            
            // VALIDATE INPUT - Check if required fields are provided
            if (username == null || username.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Username is required");
                response.put("errorCode", "MISSING_USERNAME");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (password == null || password.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Password is required");
                response.put("errorCode", "MISSING_PASSWORD");
                return ResponseEntity.badRequest().body(response);
            }
            
            // LOG LOGIN ATTEMPT - For debugging and monitoring
            System.out.println("🔐 Login attempt for user: " + username);
            System.out.println("Remember me: " + ("true".equals(rememberMe) ? "Yes" : "No"));
            
            // AUTHENTICATE USER - Call database authentication method
            boolean isAuthenticated = DatabaseConnection.authenticateUser(username, password);
            
            if (isAuthenticated) {
                // SUCCESS RESPONSE - Login successful
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("username", username);
                
                // ADD USER SESSION DATA - Information for frontend to use
                response.put("userInfo", getUserInfo(username));
                
                // TOKEN GENERATION - In production, generate JWT token here
                // response.put("token", generateJWTToken(username));
                
                System.out.println("✅ Login successful for user: " + username);
                return ResponseEntity.ok(response);
                
            } else {
                // FAILURE RESPONSE - Login failed
                response.put("success", false);
                response.put("message", "Invalid username or password");
                response.put("errorCode", "INVALID_CREDENTIALS");
                
                System.out.println("❌ Login failed for user: " + username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
        } catch (Exception e) {
            // ERROR RESPONSE - Something went wrong
            response.put("success", false);
            response.put("message", "An error occurred during login. Please try again.");
            response.put("errorCode", "INTERNAL_ERROR");
            
            System.err.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * LOGOUT ENDPOINT - Handles user logout
     * 
     * URL: POST /api/auth/logout
     * This endpoint handles user logout and session cleanup
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody Map<String, String> logoutRequest) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = logoutRequest.get("username");
            
            // LOG LOGOUT ATTEMPT
            System.out.println("🚪 Logout request for user: " + username);
            
            // CLEANUP SESSION DATA - Clear any server-side session data
            // In production, invalidate JWT tokens or clear session
            
            // SUCCESS RESPONSE
            response.put("success", true);
            response.put("message", "Logout successful");
            
            System.out.println("✅ Logout successful for user: " + username);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            // ERROR RESPONSE
            response.put("success", false);
            response.put("message", "An error occurred during logout");
            
            System.err.println("❌ Logout error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * FORGOT PASSWORD ENDPOINT - Handles password reset requests
     * 
     * URL: POST /api/auth/forgot-password
     * This endpoint initiates password reset process
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody Map<String, String> request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String email = request.get("email");
            
            // VALIDATE EMAIL
            if (email == null || email.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            // LOG FORGOT PASSWORD REQUEST
            System.out.println("🔑 Forgot password request for email: " + email);
            
            // TODO: IMPLEMENT PASSWORD RESET LOGIC
            // 1. Check if email exists in database
            // 2. Generate password reset token
            // 3. Send reset email to user
            // 4. Store token with expiration time
            
            // TEMPORARY SUCCESS RESPONSE
            response.put("success", true);
            response.put("message", "If the email exists, a password reset link has been sent");
            
            System.out.println("✅ Forgot password email sent (if email exists): " + email);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            // ERROR RESPONSE
            response.put("success", false);
            response.put("message", "An error occurred while processing your request");
            
            System.err.println("❌ Forgot password error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * CHECK SESSION ENDPOINT - Validates if user session is still active
     * 
     * URL: GET /api/auth/check-session
     * Frontend can call this to check if user is still logged in
     */
    @GetMapping("/check-session")
    public ResponseEntity<Map<String, Object>> checkSession(@RequestParam String username) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // TODO: IMPLEMENT SESSION VALIDATION
            // In production, validate JWT token or server session
            
            // TEMPORARY IMPLEMENTATION - Always return valid
            response.put("success", true);
            response.put("message", "Session is valid");
            response.put("username", username);
            response.put("userInfo", getUserInfo(username));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            // ERROR RESPONSE
            response.put("success", false);
            response.put("message", "Session validation failed");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    /**
     * GET USER INFO - Helper method to fetch user details
     * This method gets additional user information for the frontend
     * 
     * @param username The username to get info for
     * @return Map containing user information
     */
    private Map<String, Object> getUserInfo(String username) {
        Map<String, Object> userInfo = new HashMap<>();
        
        try {
            // TODO: FETCH FROM DATABASE
            // In production, query database for user details
            
            // SAMPLE USER INFO - CHANGEABLE: Replace with actual database query
            userInfo.put("username", username);
            userInfo.put("fullName", "Dr. Faculty Member");  // Get from database
            userInfo.put("email", username + "@rnscollege.edu");
            userInfo.put("department", "Computer Science");   // Get from database
            userInfo.put("role", "Faculty");
            userInfo.put("lastLogin", System.currentTimeMillis());
            
        } catch (Exception e) {
            System.err.println("❌ Error fetching user info: " + e.getMessage());
        }
        
        return userInfo;
    }
    
    /**
     * HEALTH CHECK ENDPOINT - Tests if the API is working
     * 
     * URL: GET /api/auth/health
     * Simple endpoint to test if the server is running
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Login API is running");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}

/*
  INTEGRATION INSTRUCTIONS FOR NON-CODERS:
  
  1. SPRING BOOT SETUP:
     - Create new Spring Boot project
     - Add dependencies: spring-boot-starter-web, mysql-connector-java
     - Add this file to src/main/java/com/college/booking/controller/
     - Add DatabaseConnection.java to src/main/java/com/college/booking/database/
  
  2. APPLICATION.PROPERTIES SETUP:
     Create src/main/resources/application.properties with:
     
     server.port=8080
     spring.datasource.url=jdbc:mysql://localhost:3306/seminar_booking
     spring.datasource.username=root
     spring.datasource.password=your_password
     spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  
  3. FRONTEND INTEGRATION:
     Update your React LoginForm.tsx handleSubmit function:
     
     const response = await fetch('http://localhost:8080/api/auth/login', {
       method: 'POST',
       headers: {
         'Content-Type': 'application/json',
       },
       body: JSON.stringify({
         username: username,
         password: password,
         rememberMe: rememberMe.toString()
       })
     });
     
     const result = await response.json();
     
     if (result.success) {
       // Login successful - redirect to dashboard
       console.log('Login successful:', result.userInfo);
     } else {
       // Show error message
       setErrorMessage(result.message);
     }
  
  4. TESTING THE API:
     - Start Spring Boot application
     - Test with browser: http://localhost:8080/api/auth/health
     - Should return JSON with status "OK"
  
  5. API ENDPOINTS CREATED:
     - POST /api/auth/login - Handle login requests
     - POST /api/auth/logout - Handle logout requests  
     - POST /api/auth/forgot-password - Handle password reset
     - GET /api/auth/check-session - Validate user session
     - GET /api/auth/health - Test if API is running
  
  6. SECURITY CONSIDERATIONS:
     - Add CORS configuration for production
     - Implement JWT token authentication
     - Add rate limiting for login attempts
     - Use HTTPS in production
     - Validate and sanitize all input data
     - Add proper error handling and logging
  
  7. CUSTOMIZATION OPTIONS:
     - Change API base path by modifying @RequestMapping("/api/auth")
     - Add more user fields in getUserInfo method
     - Implement proper session management
     - Add role-based access control
     - Customize error messages and response formats
*/