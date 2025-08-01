/*
  DATABASE CONNECTION UTILITY CLASS - Java Backend Code
  
  INSTRUCTIONS FOR NON-CODERS:
  1. This is Java code that runs on the server (backend)
  2. You need to install Java and a database (MySQL/PostgreSQL) to use this
  3. Change the database connection details in the constants section
  4. This code handles connecting to the database and user authentication
  
  SETUP REQUIREMENTS:
  - Java 8 or higher installed
  - MySQL or PostgreSQL database server
  - JDBC driver for your database
  - Database created with user table
*/

package com.college.booking.database;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * DATABASE CONNECTION AND USER AUTHENTICATION CLASS
 * This class handles all database operations for the faculty login system
 */
public class DatabaseConnection {
    
    // DATABASE CONNECTION CONSTANTS - CHANGE THESE FOR YOUR DATABASE
    private static final String DB_URL = "jdbc:mysql://localhost:3306/seminar_booking"; 
    // CHANGEABLE: Replace with your database URL
    // Format: jdbc:mysql://[server]:[port]/[database_name]
    
    private static final String DB_USERNAME = "root";           // CHANGEABLE: Your database username
    private static final String DB_PASSWORD = "your_password";  // CHANGEABLE: Your database password
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver"; // CHANGEABLE: Database driver class
    
    // CONNECTION OBJECT - Holds the database connection
    private static Connection connection = null;
    
    /**
     * ESTABLISH DATABASE CONNECTION
     * This method creates a connection to the database
     * 
     * @return Connection object or null if connection fails
     */
    public static Connection getConnection() {
        try {
            // LOAD DATABASE DRIVER - Required for database communication
            Class.forName(DB_DRIVER);
            
            // CREATE CONNECTION - Connect to the database using credentials
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            
            System.out.println("✅ Database connection successful!");
            return connection;
            
        } catch (ClassNotFoundException e) {
            // ERROR: Database driver not found
            System.err.println("❌ Database driver not found: " + e.getMessage());
            System.err.println("💡 Make sure to add MySQL/PostgreSQL JDBC driver to your project");
            
        } catch (SQLException e) {
            // ERROR: Database connection failed
            System.err.println("❌ Database connection failed: " + e.getMessage());
            System.err.println("💡 Check your database URL, username, and password");
            System.err.println("💡 Make sure your database server is running");
        }
        
        return null;
    }
    
    /**
     * AUTHENTICATE FACULTY USER
     * This method checks if the provided username and password are valid
     * 
     * @param username The faculty username
     * @param password The faculty password (plain text)
     * @return true if authentication successful, false otherwise
     */
    public static boolean authenticateUser(String username, String password) {
        Connection conn = getConnection();
        
        if (conn == null) {
            System.err.println("❌ Cannot authenticate - database connection failed");
            return false;
        }
        
        try {
            // SQL QUERY - Search for user with matching username
            String query = "SELECT faculty_id, username, password_hash, is_active FROM faculty_users WHERE username = ?";
            
            // PREPARED STATEMENT - Prevents SQL injection attacks
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);  // Set the username parameter
            
            // EXECUTE QUERY - Run the database search
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                // USER FOUND - Check if account is active
                boolean isActive = resultSet.getBoolean("is_active");
                
                if (!isActive) {
                    System.out.println("❌ Account is deactivated for user: " + username);
                    return false;
                }
                
                // GET STORED PASSWORD HASH - Encrypted password from database
                String storedPasswordHash = resultSet.getString("password_hash");
                
                // HASH THE PROVIDED PASSWORD - Encrypt the input password
                String inputPasswordHash = hashPassword(password);
                
                // COMPARE PASSWORDS - Check if they match
                if (storedPasswordHash.equals(inputPasswordHash)) {
                    System.out.println("✅ Authentication successful for user: " + username);
                    
                    // LOG SUCCESSFUL LOGIN - Record login attempt
                    logLoginAttempt(resultSet.getInt("faculty_id"), true);
                    
                    return true;
                } else {
                    System.out.println("❌ Invalid password for user: " + username);
                    
                    // LOG FAILED LOGIN - Record failed attempt
                    logLoginAttempt(resultSet.getInt("faculty_id"), false);
                }
            } else {
                System.out.println("❌ User not found: " + username);
            }
            
            // CLOSE DATABASE RESOURCES
            resultSet.close();
            statement.close();
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Database error during authentication: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * HASH PASSWORD FUNCTION
     * This method encrypts passwords using SHA-256 algorithm
     * 
     * @param password Plain text password
     * @return Encrypted password hash
     */
    public static String hashPassword(String password) {
        try {
            // CREATE MESSAGE DIGEST - SHA-256 encryption algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // HASH THE PASSWORD - Convert to encrypted bytes
            byte[] hashedBytes = digest.digest(password.getBytes());
            
            // ENCODE TO STRING - Convert bytes to readable string
            return Base64.getEncoder().encodeToString(hashedBytes);
            
        } catch (NoSuchAlgorithmException e) {
            System.err.println("❌ Password hashing failed: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * LOG LOGIN ATTEMPTS
     * This method records login attempts for security monitoring
     * 
     * @param facultyId The ID of the faculty member
     * @param successful Whether the login was successful
     */
    private static void logLoginAttempt(int facultyId, boolean successful) {
        Connection conn = getConnection();
        
        if (conn == null) {
            return;
        }
        
        try {
            // SQL INSERT - Add login attempt to log table
            String query = "INSERT INTO login_logs (faculty_id, login_time, successful, ip_address) VALUES (?, NOW(), ?, ?)";
            
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, facultyId);
            statement.setBoolean(2, successful);
            statement.setString(3, "127.0.0.1"); // CHANGEABLE: Get actual IP address
            
            // EXECUTE INSERT - Save the log entry
            statement.executeUpdate();
            
            // CLOSE RESOURCES
            statement.close();
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Failed to log login attempt: " + e.getMessage());
        }
    }
    
    /**
     * CREATE INITIAL DATABASE TABLES
     * This method creates the required tables if they don't exist
     * Run this once when setting up the system
     */
    public static void createTables() {
        Connection conn = getConnection();
        
        if (conn == null) {
            System.err.println("❌ Cannot create tables - database connection failed");
            return;
        }
        
        try {
            Statement statement = conn.createStatement();
            
            // CREATE FACULTY USERS TABLE - Updated with new fields for registration
            String createFacultyTable = """
                CREATE TABLE IF NOT EXISTS faculty_users (
                    faculty_id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password_hash VARCHAR(255) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    full_name VARCHAR(100) NOT NULL,
                    phone_number VARCHAR(15),
                    date_of_birth DATE,
                    faculty_id_number VARCHAR(20) UNIQUE,
                    department VARCHAR(50) NOT NULL,
                    designation VARCHAR(50),
                    years_experience INT,
                    registration_status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
                    is_active BOOLEAN DEFAULT FALSE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    last_login TIMESTAMP,
                    approved_by INT,
                    approved_at TIMESTAMP
                )
                """;
            
            // CREATE LOGIN LOGS TABLE
            String createLogsTable = """
                CREATE TABLE IF NOT EXISTS login_logs (
                    log_id INT PRIMARY KEY AUTO_INCREMENT,
                    faculty_id INT,
                    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    successful BOOLEAN NOT NULL,
                    ip_address VARCHAR(45),
                    FOREIGN KEY (faculty_id) REFERENCES faculty_users(faculty_id)
                )
                """;
            
            // EXECUTE TABLE CREATION
            statement.execute(createFacultyTable);
            statement.execute(createLogsTable);
            
            System.out.println("✅ Database tables created successfully!");
            
            // CLOSE RESOURCES
            statement.close();
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Failed to create tables: " + e.getMessage());
        }
    }
    
    /**
     * ADD SAMPLE FACULTY USER
     * This method adds a test user to the database
     * Password will be hashed automatically
     */
    public static void addSampleUser() {
        Connection conn = getConnection();
        
        if (conn == null) {
            return;
        }
        
        try {
            // SAMPLE USER DATA - CHANGEABLE: Modify for your test user
            String username = "faculty001";
            String password = "password123";  // This will be hashed
            String email = "faculty001@rnscollege.edu";
            String fullName = "Dr. John Smith";
            String department = "Computer Science";
            
            // HASH THE PASSWORD
            String hashedPassword = hashPassword(password);
            
            // INSERT QUERY
            String query = """
                INSERT INTO faculty_users (username, password_hash, email, full_name, department) 
                VALUES (?, ?, ?, ?, ?)
                """;
            
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, email);
            statement.setString(4, fullName);
            statement.setString(5, department);
            
            // EXECUTE INSERT
            int result = statement.executeUpdate();
            
            if (result > 0) {
                System.out.println("✅ Sample user added successfully!");
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
            }
            
            // CLOSE RESOURCES
            statement.close();
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Failed to add sample user: " + e.getMessage());
        }
    }
    
    /**
     * MAIN METHOD - For testing the database connection
     * Run this to test your database setup
     */
    public static void main(String[] args) {
        System.out.println("🚀 Testing database connection...");
        
        // TEST CONNECTION
        Connection testConn = getConnection();
        if (testConn != null) {
            System.out.println("✅ Database connection test successful!");
            
            // UNCOMMENT THESE LINES TO SET UP DATABASE FOR FIRST TIME:
            // createTables();        // Creates the required tables
            // addSampleUser();       // Adds a test user
            
            // TEST AUTHENTICATION
            System.out.println("\n🔐 Testing authentication...");
            boolean authResult = authenticateUser("faculty001", "password123");
            System.out.println("Authentication result: " + (authResult ? "✅ SUCCESS" : "❌ FAILED"));
            
            try {
                testConn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}

/*
  SETUP INSTRUCTIONS FOR NON-CODERS:
  
  1. INSTALL REQUIRED SOFTWARE:
     - Install Java Development Kit (JDK) 8 or higher
     - Install MySQL or PostgreSQL database server
     - Download JDBC driver for your database
  
  2. DATABASE SETUP:
     - Create a new database called "seminar_booking"
     - Update the constants at the top of this file with your database details
     - Run the main method to create tables and add sample user
  
  3. TESTING:
     - Compile: javac DatabaseConnection.java
     - Run: java DatabaseConnection
     - You should see successful connection and authentication messages
  
  4. INTEGRATION WITH FRONTEND:
     - Create REST API endpoints that call these methods
     - Frontend login form will send requests to these endpoints
     - Use frameworks like Spring Boot for easier web development
  
  5. SECURITY NOTES:
     - Never store passwords in plain text
     - Always use prepared statements to prevent SQL injection
     - Consider using stronger hashing algorithms like bcrypt
     - Implement rate limiting for login attempts
     - Use HTTPS in production
  
  6. CUSTOMIZATION:
     - Change database connection details in constants section
     - Modify table structures in createTables method
     - Add more fields to faculty_users table as needed
     - Implement password reset functionality
     - Add role-based access control
*/