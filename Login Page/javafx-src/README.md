# Faculty Booking System - JavaFX Version

A comprehensive desktop application for faculty seminar hall booking built with JavaFX, featuring an elegant interface, department-based authentication, and theme switching capabilities.

## 🎯 Features

- **Elegant Login System** with department selection (BCA, BBA, BCOM)
- **Comprehensive Registration** with personal and professional information
- **Light/Dark Theme Toggle** with persistent user preferences
- **Remember Me Functionality** for convenient login
- **Form Validation** with real-time feedback
- **Responsive Design** that works on different screen sizes
- **Database Integration** with MySQL support
- **Professional UI** matching college branding

## 📋 Prerequisites

Before running the application, ensure you have:

- **Java 11 or higher** installed
- **Maven 3.6 or higher** for dependency management
- **MySQL 8.0 or higher** for database (or PostgreSQL/SQLite)
- **IDE** (IntelliJ IDEA, Eclipse, or NetBeans) recommended

## 🚀 Quick Start

### 1. Clone or Download the Project

```bash
# If using Git
git clone <repository-url>
cd faculty-booking-system

# Or download and extract the ZIP file
```

### 2. Database Setup

1. **Install MySQL** and create a database:
```sql
CREATE DATABASE faculty_booking_system;
```

2. **Update database credentials** in `src/main/java/com/college/booking/DatabaseConnection.java`:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/faculty_booking_system";
private static final String DB_USERNAME = "your_username";
private static final String DB_PASSWORD = "your_password";
```

3. **Run the application** - tables will be created automatically on first startup.

### 3. Build and Run

```bash
# Compile the application
mvn clean compile

# Run the application
mvn javafx:run

# Or create an executable JAR
mvn clean package
java -jar target/faculty-booking-system-1.0.0.jar
```

## 💻 IDE Setup

### IntelliJ IDEA

1. **Open Project**: File → Open → Select the project folder
2. **Configure Run Configuration**:
   - Go to Run → Edit Configurations
   - Create new Application configuration
   - Main class: `com.college.booking.FacultyBookingApplication`
   - VM options: `--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml`

### Eclipse

1. **Import Project**: File → Import → Existing Maven Projects
2. **Configure Run Configuration**:
   - Right-click project → Run As → Java Application
   - Main class: `com.college.booking.FacultyBookingApplication`
   - Arguments tab → VM arguments: Add JavaFX module path

### NetBeans

1. **Open Project**: File → Open Project → Select project folder
2. **Project Properties** → Run → Add VM arguments for JavaFX modules

## 🎨 Customization Guide

### Changing College Information

1. **College Name and System Title**:
   - Edit `MainController.java` constants:
   ```java
   private static final String COLLEGE_NAME = "Your College Name";
   private static final String SYSTEM_NAME = "Your System Name";
   ```

2. **College Logo**:
   - Replace `src/main/resources/images/college-logo.png`
   - Update `LOGO_PATH` in `MainController.java` if needed

3. **Colors and Theme**:
   - Modify `light-theme.css` and `dark-theme.css`
   - Change primary color (#1e3a8a) to your college colors

### Adding New Departments

1. **Update Department Arrays** in both controllers:
```java
private static final Department[] DEPARTMENTS = {
    new Department("CS", "CS", "Computer Science"),
    new Department("EE", "EE", "Electrical Engineering"),
    // Add your departments here
};
```

2. **Update Database Validation** in `LoginController.java` and `SignupController.java`

### Adding New Faculty Designations

Update the `DESIGNATIONS` array in `SignupController.java`:
```java
private static final Designation[] DESIGNATIONS = {
    new Designation("professor", "Professor"),
    new Designation("your_new_designation", "Your New Title"),
    // Add more as needed
};
```

## 📁 Project Structure

```
javafx-src/
├── main/
│   ├── java/com/college/booking/
│   │   ├── FacultyBookingApplication.java     # Main application entry point
│   │   ├── ThemeManager.java                  # Theme management
│   │   └── controllers/
│   │       ├── MainController.java            # Main window controller
│   │       ├── LoginController.java           # Login form controller
│   │       └── SignupController.java          # Registration form controller
│   └── resources/
│       ├── css/
│       │   ├── base-styles.css               # Common styling
│       │   ├── light-theme.css               # Light theme colors
│       │   └── dark-theme.css                # Dark theme colors
│       ├── fxml/
│       │   ├── MainView.fxml                 # Main window layout
│       │   ├── LoginForm.fxml                # Login form layout
│       │   └── SignupForm.fxml               # Registration form layout
│       └── images/
│           └── college-logo.png              # College logo
├── pom.xml                                   # Maven configuration
└── module-info.java                          # Java module configuration
```

## 🗄️ Database Schema

The application automatically creates these tables:

### faculty_users
- `faculty_id` (Primary Key)
- `username`, `password_hash`, `email`
- `full_name`, `phone_number`, `date_of_birth`
- `faculty_id_number`, `department`, `designation`
- `years_experience`, `registration_status`
- `is_active`, `created_at`, `last_login`

## 🔧 Configuration Options

### Theme Customization

1. **Add New Themes**:
   - Create new CSS file (e.g., `blue-theme.css`)
   - Add to `ThemeManager.java` enum
   - Implement theme switching logic

2. **Modify Existing Themes**:
   - Edit `light-theme.css` or `dark-theme.css`
   - Change color values while maintaining variable names

### Database Configuration

1. **Switch to PostgreSQL**:
   - Uncomment PostgreSQL dependency in `pom.xml`
   - Update connection URL in `DatabaseConnection.java`

2. **Use SQLite for Development**:
   - Uncomment SQLite dependency in `pom.xml`
   - Change connection URL to SQLite format

## 🧪 Testing

Run the test suite:
```bash
mvn test
```

Add new tests in `src/test/java/` following JUnit conventions.

## 🚀 Deployment

### Create Standalone JAR

```bash
mvn clean package
```

The executable JAR will be in `target/faculty-booking-system-1.0.0.jar`

### Distribution

1. **Include JavaFX Runtime**: Use jlink to create custom runtime
2. **Database Setup**: Provide database creation scripts
3. **Configuration**: Include sample configuration files

## 🐛 Troubleshooting

### Common Issues

1. **JavaFX Runtime Not Found**:
   - Ensure JavaFX is in module path
   - Check JVM arguments in run configuration

2. **Database Connection Failed**:
   - Verify MySQL is running
   - Check credentials in `DatabaseConnection.java`
   - Ensure database exists

3. **FXML Load Error**:
   - Check FXML file paths in controllers
   - Verify fx:id matches @FXML annotations

4. **CSS Not Applied**:
   - Check CSS file paths in `ThemeManager.java`
   - Verify CSS syntax and class names

### Performance Issues

1. **Slow Startup**:
   - Check database connection timeout
   - Optimize FXML loading

2. **Memory Usage**:
   - Adjust JVM heap size: `-Xmx512m`
   - Profile with Java profiling tools

## 📚 Additional Resources

- [JavaFX Documentation](https://openjfx.io/)
- [FXML Reference](https://docs.oracle.com/javafx/2/fxml_get_started/jfxpub-fxml_get_started.htm)
- [Maven JavaFX Plugin](https://github.com/openjfx/javafx-maven-plugin)
- [CSS Reference for JavaFX](https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html)

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📞 Support

For support and questions:
- Create an issue in the repository
- Check the troubleshooting section
- Review JavaFX documentation

---

**Note**: This is a desktop application designed for institutional use. Ensure proper security measures are in place for production deployment.