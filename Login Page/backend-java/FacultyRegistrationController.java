/*
  FACULTY REGISTRATION API CONTROLLER - Java Spring Boot REST API for Signup
  
  INSTRUCTIONS FOR NON-CODERS:
  1. This creates web APIs that the frontend signup form can call
  2. Install Spring Boot framework to use this code  
  3. This handles HTTP requests from the signup form
  4. Connects the React frontend with the Java database code
  
  SETUP REQUIREMENTS:
  - Spring Boot framework
  - Spring Web dependency
  - DatabaseConnection.java file from previous code
  - Java Bean Validation dependencies
*/

package com.college.booking.controller;

import com.college.booking.database.DatabaseConnection;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.sql.*;

/**
 * FACULTY REGISTRATION API CONTROLLER
 * This class creates web endpoints for faculty registration
 * It receives HTTP requests from the signup form and returns JSON responses
 */
@RestController
@RequestMapping("/api/faculty")                       // Base URL path for faculty-related endpoints
@CrossOrigin(origins = "http://localhost:3000")       // CHANGEABLE: Allow frontend to call these APIs
public class FacultyRegistrationController {
    
    // VALIDATION PATTERNS - Regular expressions for input validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[6-9]\\d{9}$"  // Indian mobile number format
    );
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._-]{4,20}$"  // Alphanumeric with some special chars, 4-20 length
    );
    
    /**
     * FACULTY REGISTRATION ENDPOINT - Handles faculty signup requests
     * 
     * URL: POST /api/faculty/register
     * This endpoint receives faculty registration data from frontend
     * and creates a new faculty account
     * 
     * @param registrationRequest Map containing all faculty details
     * @return JSON response with registration result
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerFaculty(@RequestBody Map<String, Object> registrationRequest) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // EXTRACT REGISTRATION DATA - Get all fields from request
            
            // Personal Information
            String fullName = (String) registrationRequest.get("fullName");
            String email = (String) registrationRequest.get("email");
            String phone = (String) registrationRequest.get("phone");
            String dateOfBirth = (String) registrationRequest.get("dateOfBirth");
            
            // Professional Information
            String facultyId = (String) registrationRequest.get("facultyId");
            String department = (String) registrationRequest.get("department");
            String designation = (String) registrationRequest.get("designation");
            String experience = (String) registrationRequest.get("experience");
            
            // Account Information
            String username = (String) registrationRequest.get("username");
            String password = (String) registrationRequest.get("password");
            String confirmPassword = (String) registrationRequest.get("confirmPassword");
            Boolean agreeToTerms = (Boolean) registrationRequest.get("agreeToTerms");
            
            // VALIDATE REQUIRED FIELDS
            if (fullName == null || fullName.trim().isEmpty()) {
                return createErrorResponse("Full name is required", "MISSING_FULL_NAME");
            }
            
            if (email == null || email.trim().isEmpty()) {
                return createErrorResponse("Email address is required", "MISSING_EMAIL");
            }
            
            if (phone == null || phone.trim().isEmpty()) {
                return createErrorResponse("Phone number is required", "MISSING_PHONE");
            }
            
            if (department == null || department.trim().isEmpty()) {
                return createErrorResponse("Department selection is required", "MISSING_DEPARTMENT");
            }
            
            if (designation == null || designation.trim().isEmpty()) {
                return createErrorResponse("Designation is required", "MISSING_DESIGNATION");
            }
            
            if (experience == null || experience.trim().isEmpty()) {
                return createErrorResponse("Years of experience is required", "MISSING_EXPERIENCE");
            }
            
            if (username == null || username.trim().isEmpty()) {
                return createErrorResponse("Username is required", "MISSING_USERNAME");
            }
            
            if (password == null || password.trim().isEmpty()) {
                return createErrorResponse("Password is required", "MISSING_PASSWORD");
            }
            
            if (confirmPassword == null || !password.equals(confirmPassword)) {
                return createErrorResponse("Passwords do not match", "PASSWORD_MISMATCH");
            }
            
            if (agreeToTerms == null || !agreeToTerms) {
                return createErrorResponse("You must agree to the terms and conditions", "TERMS_NOT_ACCEPTED");
            }
            
            // VALIDATE EMAIL FORMAT
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                return createErrorResponse("Please enter a valid email address", "INVALID_EMAIL");
            }
            
            // VALIDATE PHONE FORMAT
            if (!PHONE_PATTERN.matcher(phone).matches()) {
                return createErrorResponse("Please enter a valid 10-digit phone number", "INVALID_PHONE");
            }
            
            // VALIDATE USERNAME FORMAT
            if (!USERNAME_PATTERN.matcher(username).matches()) {
                return createErrorResponse("Username must be 4-20 characters with letters, numbers, dots, underscores, or hyphens only", "INVALID_USERNAME");
            }
            
            // VALIDATE PASSWORD STRENGTH
            if (password.length() < 8) {
                return createErrorResponse("Password must be at least 8 characters long", "WEAK_PASSWORD");
            }
            
            // VALIDATE DEPARTMENT
            if (!isValidDepartment(department)) {
                return createErrorResponse("Invalid department selected", "INVALID_DEPARTMENT");
            }
            
            // VALIDATE DESIGNATION
            if (!isValidDesignation(designation)) {
                return createErrorResponse("Invalid designation selected", "INVALID_DESIGNATION");
            }
            
            // CHECK IF EMAIL OR USERNAME ALREADY EXISTS
            if (isEmailExists(email)) {
                return createErrorResponse("Email address is already registered", "EMAIL_EXISTS");
            }
            
            if (isUsernameExists(username)) {
                return createErrorResponse("Username is already taken", "USERNAME_EXISTS");
            }
            
            // VALIDATE FACULTY ID IF PROVIDED
            if (facultyId != null && !facultyId.trim().isEmpty() && isFacultyIdExists(facultyId)) {
                return createErrorResponse("Faculty ID is already registered", "FACULTY_ID_EXISTS");
            }
            
            // LOG REGISTRATION ATTEMPT
            System.out.println("🎓 Registration attempt for: " + fullName + " (" + email + ")");
            System.out.println("Department: " + department + ", Designation: " + designation);
            
            // CREATE FACULTY ACCOUNT - Call database method to save faculty data
            boolean registrationSuccess = createFacultyAccount(
                fullName, email, phone, dateOfBirth,
                facultyId, department, designation, experience,
                username, password
            );
            
            if (registrationSuccess) {
                // SUCCESS RESPONSE
                response.put("success", true);
                response.put("message", "Faculty account created successfully! Please wait for admin approval.");
                response.put("username", username);
                response.put("email", email);
                
                System.out.println("✅ Registration successful for: " + fullName);
                return ResponseEntity.ok(response);
                
            } else {
                // DATABASE ERROR
                response.put("success", false);
                response.put("message", "Failed to create account. Please try again.");
                response.put("errorCode", "DATABASE_ERROR");
                
                System.out.println("❌ Registration failed for: " + fullName);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (Exception e) {
            // UNEXPECTED ERROR
            System.err.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "An unexpected error occurred during registration. Please try again.");
            response.put("errorCode", "INTERNAL_ERROR");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * CHECK USERNAME AVAILABILITY ENDPOINT
     * 
     * URL: GET /api/faculty/check-username?username=example
     * This endpoint checks if a username is available for registration
     */
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsernameAvailability(@RequestParam String username) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // VALIDATE USERNAME FORMAT
            if (!USERNAME_PATTERN.matcher(username).matches()) {
                response.put("available", false);
                response.put("message", "Username must be 4-20 characters with letters, numbers, dots, underscores, or hyphens only");
                return ResponseEntity.ok(response);
            }
            
            // CHECK AVAILABILITY
            boolean isAvailable = !isUsernameExists(username);
            
            response.put("available", isAvailable);
            response.put("message", isAvailable ? "Username is available" : "Username is already taken");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("available", false);
            response.put("message", "Error checking username availability");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * CHECK EMAIL AVAILABILITY ENDPOINT
     * 
     * URL: GET /api/faculty/check-email?email=example@college.edu
     * This endpoint checks if an email is available for registration
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmailAvailability(@RequestParam String email) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // VALIDATE EMAIL FORMAT
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                response.put("available", false);
                response.put("message", "Please enter a valid email address");
                return ResponseEntity.ok(response);
            }
            
            // CHECK AVAILABILITY
            boolean isAvailable = !isEmailExists(email);
            
            response.put("available", isAvailable);
            response.put("message", isAvailable ? "Email is available" : "Email is already registered");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("available", false);
            response.put("message", "Error checking email availability");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * HELPER METHOD - Create error response
     */
    private ResponseEntity<Map<String, Object>> createErrorResponse(String message, String errorCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("errorCode", errorCode);
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * HELPER METHOD - Validate department
     */
    private boolean isValidDepartment(String department) {
        // CHANGEABLE: Add or remove valid departments here
        return department.equals("BCA") || department.equals("BBA") || department.equals("BCOM");
    }
    
    /**
     * HELPER METHOD - Validate designation
     */
    private boolean isValidDesignation(String designation) {
        // CHANGEABLE: Add or remove valid designations here
        return designation.equals("professor") || 
               designation.equals("associate_professor") || 
               designation.equals("assistant_professor") || 
               designation.equals("lecturer") || 
               designation.equals("visiting_faculty") || 
               designation.equals("guest_lecturer");
    }
    
    /**
     * DATABASE METHOD - Check if email exists
     */
    private boolean isEmailExists(String email) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return false;
        
        try {
            String query = "SELECT COUNT(*) FROM faculty_users WHERE email = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            resultSet.close();
            statement.close();
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * DATABASE METHOD - Check if username exists
     */
    private boolean isUsernameExists(String username) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return false;
        
        try {
            String query = "SELECT COUNT(*) FROM faculty_users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            resultSet.close();
            statement.close();
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("Error checking username existence: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * DATABASE METHOD - Check if faculty ID exists
     */
    private boolean isFacultyIdExists(String facultyId) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return false;
        
        try {
            String query = "SELECT COUNT(*) FROM faculty_users WHERE faculty_id_number = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, facultyId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            resultSet.close();
            statement.close();
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("Error checking faculty ID existence: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * DATABASE METHOD - Create faculty account
     */
    private boolean createFacultyAccount(String fullName, String email, String phone, String dateOfBirth,
                                       String facultyId, String department, String designation, String experience,
                                       String username, String password) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return false;
        
        try {
            // HASH PASSWORD - Use the same method from DatabaseConnection
            String hashedPassword = DatabaseConnection.hashPassword(password);
            if (hashedPassword == null) return false;
            
            // INSERT FACULTY DATA
            String query = """
                INSERT INTO faculty_users (
                    username, password_hash, email, full_name, phone_number, date_of_birth,
                    faculty_id_number, department, designation, years_experience, 
                    registration_status, is_active, created_at
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'pending', false, NOW())
                """;
            
            PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, email);
            statement.setString(4, fullName);
            statement.setString(5, phone);
            
            // Handle optional date of birth
            if (dateOfBirth != null && !dateOfBirth.trim().isEmpty()) {
                statement.setDate(6, Date.valueOf(dateOfBirth));
            } else {
                statement.setNull(6, Types.DATE);
            }
            
            // Handle optional faculty ID
            if (facultyId != null && !facultyId.trim().isEmpty()) {
                statement.setString(7, facultyId);
            } else {
                statement.setNull(7, Types.VARCHAR);
            }
            
            statement.setString(8, department);
            statement.setString(9, designation);
            statement.setInt(10, Integer.parseInt(experience));
            
            // EXECUTE INSERT
            int result = statement.executeUpdate();
            
            if (result > 0) {
                // GET GENERATED FACULTY ID
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newFacultyId = generatedKeys.getInt(1);
                    System.out.println("✅ Faculty account created with ID: " + newFacultyId);
                }
                generatedKeys.close();
            }
            
            statement.close();
            conn.close();
            
            return result > 0;
            
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Error creating faculty account: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

/*
  INTEGRATION INSTRUCTIONS FOR NON-CODERS:
  
  1. DATABASE TABLE UPDATES:
     Add these columns to your faculty_users table:
     
     ALTER TABLE faculty_users ADD COLUMN phone_number VARCHAR(15);
     ALTER TABLE faculty_users ADD COLUMN date_of_birth DATE;
     ALTER TABLE faculty_users ADD COLUMN faculty_id_number VARCHAR(20) UNIQUE;
     ALTER TABLE faculty_users ADD COLUMN designation VARCHAR(50);
     ALTER TABLE faculty_users ADD COLUMN years_experience INT;
     ALTER TABLE faculty_users ADD COLUMN registration_status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending';
     
  2. FRONTEND INTEGRATION:
     Update your React SignupForm.tsx handleSubmit function:
     
     const response = await fetch('http://localhost:8080/api/faculty/register', {
       method: 'POST',
       headers: { 'Content-Type': 'application/json' },
       body: JSON.stringify({
         fullName, email, phone, dateOfBirth,
         facultyId, department: selectedDepartment, designation, experience,
         username, password, confirmPassword, agreeToTerms
       })
     });
     
     const result = await response.json();
     
     if (result.success) {
       setSuccessMessage(result.message);
     } else {
       setErrorMessage(result.message);
     }
  
  3. API ENDPOINTS CREATED:
     - POST /api/faculty/register - Handle faculty registration
     - GET /api/faculty/check-username?username=example - Check username availability
     - GET /api/faculty/check-email?email=example@college.edu - Check email availability
  
  4. VALIDATION FEATURES:
     - Email format validation
     - Phone number validation (Indian format)
     - Username format validation
     - Password strength validation
     - Department and designation validation
     - Duplicate email/username checking
  
  5. SECURITY FEATURES:
     - Password hashing using SHA-256
     - SQL injection prevention with prepared statements
     - Input validation and sanitization
     - Terms and conditions enforcement
  
  6. CUSTOMIZATION OPTIONS:
     - Add/remove departments in isValidDepartment method
     - Add/remove designations in isValidDesignation method
     - Modify validation patterns at the top of the class
     - Add more required fields as needed
     - Customize error messages
*/